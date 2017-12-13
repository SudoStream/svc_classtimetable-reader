package io.sudostream.classtimetablereader.dao

import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.sessions.SessionBoundaryType
import org.mongodb.scala.bson.{BsonDocument, BsonString}
import org.scalatest.FunSuite

class MongoClassTimetableBsonToModelTranslatorTest extends FunSuite with MongoClassTimetableBsonToModelTranslatorTestHelper {

  test("Calling translateBsonToClassTimetable with None returns none") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assert(translator.translateMaybeBsonToMaybeClassTimetable(TimeToTeachId("12345"), None) === None)
  }

  test("Calling translateBsonToClassTimetable with empty Bson returns none") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assert(translator.translateMaybeBsonToMaybeClassTimetable(TimeToTeachId("12345"), Some(BsonDocument())) === None)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns defined") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    println(s"maybeClassTimetable : ${maybeClassTimetable.toString}")
    assert(maybeClassTimetable.isDefined)
  }

  test("Calling extractBoundaryType with empty BsonString returns START_OF_TEACHING_SESSION") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val sessionBoundaryType = translator.extractBoundaryType(BsonString(""))
    assert(sessionBoundaryType === SessionBoundaryType.START_OF_TEACHING_SESSION)
  }

  test("Calling extractBoundaryType with empty BsonString(START_OF_TEACHING_SESSION) returns START_OF_TEACHING_SESSION") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val sessionBoundaryType = translator.extractBoundaryType(BsonString("START_OF_TEACHING_SESSION"))
    assert(sessionBoundaryType === SessionBoundaryType.START_OF_TEACHING_SESSION)
  }

  test("Calling extractBoundaryType with empty BsonString(END_OF_TEACHING_SESSION) returns END_OF_TEACHING_SESSION") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val sessionBoundaryType = translator.extractBoundaryType(BsonString("END_OF_TEACHING_SESSION"))
    assert(sessionBoundaryType === SessionBoundaryType.END_OF_TEACHING_SESSION)
  }

  test("Calling extractSessionName with empty BsonString returns NONE") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeSessionName = translator.extractSessionName(BsonString(""))
    assert(maybeSessionName.isEmpty)
  }

  test("Calling extractSessionName with empty BsonString('This Is A Session') returns is defined") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeSessionName = translator.extractSessionName(BsonString("This Is A Session"))
    assert(maybeSessionName.isDefined)
  }

  test("Calling extractSessionName with empty BsonString('This Is A Session') returns Some('This Is A Session')") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeSessionName = translator.extractSessionName(BsonString("This Is A Session"))
    assert(maybeSessionName.get.value === "This Is A Session")
  }


}
