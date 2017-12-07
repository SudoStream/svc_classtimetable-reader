package io.sudostream.classtimetablereader.dao

import io.sudostream.classtimetablereader.model.{ClassName, TimeToTeachId}
import org.mongodb.scala.bson.BsonDocument

import scala.concurrent.Future

trait MongoFindQueriesProxy {

  def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[BsonDocument]]

}
