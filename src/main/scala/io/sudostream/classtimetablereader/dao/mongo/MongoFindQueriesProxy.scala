package io.sudostream.classtimetablereader.dao.mongo

import io.sudostream.classtimetablereader.model.ClassId
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument

import scala.concurrent.Future

trait MongoFindQueriesProxy {

  def findClassTimetable(classId: ClassId, timeToTeachId: TimeToTeachId): Future[Option[BsonDocument]]

  def findTeachersClasses(timeToTeachId: TimeToTeachId): Future[List[Document]]

}
