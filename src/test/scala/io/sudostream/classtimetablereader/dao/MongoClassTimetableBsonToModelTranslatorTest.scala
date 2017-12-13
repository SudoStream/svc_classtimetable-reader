package io.sudostream.classtimetablereader.dao

import java.time.format.DateTimeParseException

import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.sessions.SessionBoundaryType
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.time.DayOfTheWeek
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonString}
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

  test("Calling createSchoolTimesFromDoc with empty array returns an empty ClassTimetableSchoolTimes") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val classTimeTableSchoolTimes = translator.createSchoolTimesFromDoc(new BsonArray())
    assert(classTimeTableSchoolTimes.schoolSessionBoundaries.isEmpty)
  }

  test("Calling createSchoolTimesFromDoc with valid array returns an non empty ClassTimetableSchoolTimes") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val classTimeTableSchoolTimes = translator.createSchoolTimesFromDoc(createSchoolTimesArray())
    assert(classTimeTableSchoolTimes.schoolSessionBoundaries.nonEmpty)
  }

  test("Calling createAllSessionsOfTheWeek with empty array returns an empty SessionOfTheDayWrapper List") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val sessionOfTheDayWrappers = translator.createAllSessionsOfTheWeek(new BsonArray())
    assert(sessionOfTheDayWrappers.isEmpty)
  }

  test("Calling createAllSessionsOfTheWeek with valid array returns a non empty SessionOfTheDayWrapper List") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val sessionOfTheDayWrappers = translator.createAllSessionsOfTheWeek(createAllSessionsOfTheWeekArray())
    assert(sessionOfTheDayWrappers.nonEmpty)
  }

  test("Calling extractDayOfTheWeek with invalid 'rubbish' throws MatchError ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assertThrows[MatchError] {
      val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("rubbish"))
    }
  }

  test("Calling extractDayOfTheWeek with 'MONDAY' returns DayOfTheWeek.MONDAY ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("MONDAY"))
    assert(dayOfTheWeek === DayOfTheWeek.MONDAY)
  }

  test("Calling extractDayOfTheWeek with 'TUESDAY' returns DayOfTheWeek.TUESDAY ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("TUESDAY"))
    assert(dayOfTheWeek === DayOfTheWeek.TUESDAY)
  }

  test("Calling extractDayOfTheWeek with 'WEDNESDAY' returns DayOfTheWeek.WEDNESDAY ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("WEDNESDAY"))
    assert(dayOfTheWeek === DayOfTheWeek.WEDNESDAY)
  }

  test("Calling extractDayOfTheWeek with 'THURSDAY' returns DayOfTheWeek.THURSDAY ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("THURSDAY"))
    assert(dayOfTheWeek === DayOfTheWeek.THURSDAY)
  }

  test("Calling extractDayOfTheWeek with 'FRIDAY' returns DayOfTheWeek.FRIDAY ") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val dayOfTheWeek = translator.extractDayOfTheWeek(BsonString("FRIDAY"))
    assert(dayOfTheWeek === DayOfTheWeek.FRIDAY)
  }

  test("Calling extractStartTime with 'nonsense' throws DateTimeParseException") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assertThrows[DateTimeParseException] {
      translator.extractStartTime(BsonString("nonsense"))
    }
  }

  test("Calling extractStartTime with '09:03' returns StartTime of the same value") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val startTime = translator.extractStartTime(BsonString("09:03"))
    assert(startTime.timeIso8601 === "09:03")
  }

  test("Calling extractEndTime with 'nonsense' throws DateTimeParseException") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assertThrows[DateTimeParseException] {
      translator.extractEndTime(BsonString("nonsense"))
    }
  }

  test("Calling extractEndTime with '15:57' returns EndTime of the same value") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val endTime = translator.extractEndTime(BsonString("15:57"))
    assert(endTime.timeIso8601 === "15:57")
  }

}
