package io.sudostream.classtimetablereader.api.http

import java.time.Instant

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, ValidationRejection}
import akka.stream.Materializer
import akka.util.Timeout
import io.sudostream.classtimetablereader.api.kafka.StreamingComponents
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import io.sudostream.classtimetablereader.dao.{ClassTimetableDao, MongoClassTimetableBsonToModelTranslator}
import io.sudostream.classtimetablereader.model.ClassId
import io.sudostream.timetoteach.kafka.serializing.systemwide.classes.ClassDetailsCollectionSerializer
import io.sudostream.timetoteach.kafka.serializing.systemwide.classtimetable.ClassTimetableSerializer
import io.sudostream.timetoteach.messages.systemwide.model.SingleClassDetailsWrapper
import io.sudostream.timetoteach.messages.systemwide.model.classes.{ClassDetails, ClassDetailsCollection}
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
      parameters('classId.?, 'timeToTeachUserId.?) {
        (classIdOption, timeToTeachUserIdOption) =>
          get {
            val initialRequestReceived = Instant.now().toEpochMilli
            log.debug(s"Looking for classId '${classIdOption.getOrElse("NONE")}' & tttId '${timeToTeachUserIdOption.getOrElse("NONE")}'")

            val classId = ClassId(classIdOption.getOrElse(""))
            val tttUserId = TimeToTeachId(timeToTeachUserIdOption.getOrElse(""))
            val futureMaybeClassTimetable: Future[Option[ClassTimetable]] = classTimetableDao.findClassTimetable(classId, tttUserId)
            processClassTimetableFuture(futureMaybeClassTimetable)
          }
      }
    } ~ path("api" / "classes" / "user" / Segment) { (specifiedTttUserId) =>
      parameters('timeToTeachUserId.?) {
        (timeToTeachUserIdOption) =>
          get {
            val initialRequestReceived = Instant.now().toEpochMilli
            timeToTeachUserIdOption match {
              case Some(timeToTeachUserId) =>
                log.debug(s"Looking for classes associated with user '$timeToTeachUserId'")
                val tttUserId = TimeToTeachId(timeToTeachUserId)
                val futureMaybeClassTimetable: Future[List[ClassDetails]] = classTimetableDao.findTeachersClasses(tttUserId)
                processClassDetailsListFuture(futureMaybeClassTimetable)
              case None =>
                reject(ValidationRejection(s"ERROR: Need to specify user id, none provided"))
            }
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

  def convertToClassDetailsCollection(classDetailses: List[ClassDetails]) : ClassDetailsCollection = {
    ClassDetailsCollection (
      classDetailses.map(classDetails => SingleClassDetailsWrapper(classDetails))
    )
  }

  private def processClassDetailsListFuture(eventualClassDetailses: Future[List[ClassDetails]]) = {
    onComplete(eventualClassDetailses) {
      case Success(classDetailses) =>
          val classDetailsCollection = convertToClassDetailsCollection(classDetailses)
          val writer = new ClassDetailsCollectionSerializer
          val classTimetableBytes = writer.serialize("ignore", classDetailsCollection)
          complete(HttpEntity(ContentTypes.`application/octet-stream`, classTimetableBytes))
      case Failure(ex) => failWith(ex)
    }
  }

}
