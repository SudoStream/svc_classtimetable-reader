package io.sudostream.classtimetablereader.dao.mongo

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.Materializer
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import io.sudostream.classtimetablereader.dao.{ClassTimetableDao, MongoClassTimetableBsonToModelTranslator}
import io.sudostream.classtimetablereader.model.ClassName
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.{ClassTimetable, TimeToTeachId}

import scala.concurrent.{ExecutionContextExecutor, Future}

sealed class MongoDbClassTimetableDao(mongoFindQueriesProxy: MongoFindQueriesProxy,
                                      actorSystemWrapper: ActorSystemWrapper,
                                      translator: MongoClassTimetableBsonToModelTranslator
                                     ) extends ClassTimetableDao {

  implicit val system: ActorSystem = actorSystemWrapper.system
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = actorSystemWrapper.materializer
  val log: LoggingAdapter = system.log

  override def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[ClassTimetable]] = {
    val eventualMaybeTimetableBson = mongoFindQueriesProxy.findClassTimetable(className, timeToTeachId)
    for {
      maybeTimetableBson <- eventualMaybeTimetableBson
      maybeClassTimetable = translator.translateMaybeBsonToMaybeClassTimetable(timeToTeachId, maybeTimetableBson)
    } yield maybeClassTimetable
  }

}