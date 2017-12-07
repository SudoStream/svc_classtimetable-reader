package io.sudostream.classtimetablereader.dao

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.Materializer
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import io.sudostream.classtimetablereader.model.{ClassName, TimeToTeachId}
import org.mongodb.scala.bson.BsonDocument

import scala.concurrent.{ExecutionContextExecutor, Future}

sealed class MongoDbUserReaderDao(mongoFindQueriesProxy: MongoFindQueriesProxy,
                                  actorSystemWrapper: ActorSystemWrapper) extends UserReaderDao {

  implicit val system: ActorSystem = actorSystemWrapper.system
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = actorSystemWrapper.materializer
  val log: LoggingAdapter = system.log

  override def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[BsonDocument]] = {
    mongoFindQueriesProxy.findClassTimetable(className,timeToTeachId)
  }

}