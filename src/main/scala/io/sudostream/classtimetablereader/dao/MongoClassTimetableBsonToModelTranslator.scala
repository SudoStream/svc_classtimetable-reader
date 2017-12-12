package io.sudostream.classtimetablereader.dao

import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.ClassTimetable
import org.mongodb.scala.bson.BsonDocument

class MongoClassTimetableBsonToModelTranslator() {

  def translateMaybeBsonToMaybeClassTimetable(maybeClassTimetableBson: Option[BsonDocument]): Option[ClassTimetable] = {
    maybeClassTimetableBson match {
      case Some(classTimetableBson) => translateBsonToMaybeClassTimetable(classTimetableBson)
      case None => None
    }
  }

  def translateBsonToMaybeClassTimetable(classTimetableBson: BsonDocument): Option[ClassTimetable] = ???

}
