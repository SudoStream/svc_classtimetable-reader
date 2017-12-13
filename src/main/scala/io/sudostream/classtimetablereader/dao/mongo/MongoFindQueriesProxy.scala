package io.sudostream.classtimetablereader.dao.mongo

import io.sudostream.classtimetablereader.model.ClassName
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import org.mongodb.scala.bson.BsonDocument

import scala.concurrent.Future

trait MongoFindQueriesProxy {

  def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[BsonDocument]]

}
