package io.sudostream.classtimetablereader.api.http

import java.time.Instant

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout
import io.sudostream.classtimetablereader.api.kafka.StreamingComponents
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import io.sudostream.classtimetablereader.dao.{ClassTimetableDao, MongoClassTimetableBsonToModelTranslator}
import io.sudostream.classtimetablereader.model.ClassName
import io.sudostream.timetoteach.kafka.serializing.systemwide.classtimetable.ClassTimetableSerializer
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.{ClassTimetable, TimeToTeachId}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class HttpRoutes(
                  classTimetableDao: ClassTimetableDao,
                  actorSystemWrapper: ActorSystemWrapper,
                  streamingComponents: StreamingComponents,
                  classTimeTablePersistenceLayer: MongoClassTimetableBsonToModelTranslator
                )
  extends Health {
  implicit val system: ActorSystem = actorSystemWrapper.system
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = actorSystemWrapper.materializer
  val log: LoggingAdapter = system.log

  implicit val timeout: Timeout = Timeout(30 seconds)

  val routes: Route =
    path("api" / "classtimetables") {
      parameters('className.?, 'timeToTeachUserId.?) {
        (classNameOption, timeToTeachUserIdOption) =>
          get {
            val initialRequestReceived = Instant.now().toEpochMilli
            log.debug(s"Looking for className '${classNameOption.getOrElse("NONE")}' & tttId '${timeToTeachUserIdOption.getOrElse("NONE")}'")

            val className = ClassName(classNameOption.getOrElse(""))
            val tttUserId = TimeToTeachId(timeToTeachUserIdOption.getOrElse(""))
            val futureMaybeClassTimetable: Future[Option[ClassTimetable]] = classTimetableDao.findClassTimetable(className, tttUserId)
            processClassTimetableFuture(futureMaybeClassTimetable)
          }
      }
    } ~ health

  private def processClassTimetableFuture(futureMaybeClassTimetable: Future[Option[ClassTimetable]]) = {
    onComplete(futureMaybeClassTimetable) {
      case Success(maybeClassTimetable) =>
        if (maybeClassTimetable.isDefined) {
          val classTimetable = maybeClassTimetable.get
          val writer = new ClassTimetableSerializer
          val classTimetableBytes = writer.serialize("ignore", classTimetable)
          complete(HttpEntity(ContentTypes.`application/octet-stream`, classTimetableBytes))
        } else {
          reject
        }
      case Failure(ex) => failWith(ex)
    }
  }
}
