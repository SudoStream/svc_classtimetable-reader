package io.sudostream.classtimetablereader.dao

import java.time.format.DateTimeParseException

import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.sessions.SessionBoundaryType
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.subjectdetail.SubjectName
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

  test("Calling translateBsonToClassTimetable with valid Bson returns timetable with tttUserId = 'user12345'") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    assert(classTimeTable.timeToTeachId.value === "user12345")
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 6 school times") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    assert(classTimeTable.schoolTimes.schoolSessionBoundaries.size === 6)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns class name = 'P3AB'") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    assert(classTimeTable.className.value === "P3AB")
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 15 sessions of the week") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    assert(classTimeTable.allSessionsOfTheWeek.size === 15)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 5 sessions with a start of 09:00") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    assert(classTimeTable.allSessionsOfTheWeek.count{ session =>
      session.sessionOfTheDay.startTime.timeIso8601 == "09:00"
    } == 5)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 1 subject detail with a subject name of ART") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.ART) == 1)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 1 subject detail with a subject name of MATHS") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.MATHS) == 1)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 1 subject detail with a subject name of GEOGRAPHY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.GEOGRAPHY) == 1)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 1 subject detail with a subject name of SPELLING") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.SPELLING) == 1)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 1 subject detail with a subject name of ICT") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.ICT) == 1)
  }

  test("Calling translateBsonToClassTimetable with valid Bson returns 0 subjects detail with a subject name of HISTORY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val maybeClassTimetable = translator.translateBsonToMaybeClassTimetable(TimeToTeachId("user12345"), createValidClassTimetableBson())
    val classTimeTable = maybeClassTimetable.get
    val allSubjectsInTheWeek = {
      for {
        session <- classTimeTable.allSessionsOfTheWeek
        subject <- session.sessionOfTheDay.subjects
      } yield subject.subjectDetail
    }

    assert(allSubjectsInTheWeek.count(subject => subject.subjectName == SubjectName.HISTORY) == 0)
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

  test("Calling extractSubjects with an empty array returns an empty list") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subjects = translator.extractSubjects(new BsonArray())
    assert(subjects.isEmpty)
  }

  test("Calling extractSubjects with a non-empty array returns a non-empty list") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subjects = translator.extractSubjects(createSomeSubjects())
    assert(subjects.nonEmpty)
  }

  test("Calling extractSubjectName with 'nonsense' throws a runtime exception") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    assertThrows[RuntimeException] {
      translator.extractSubjectName(BsonString("nonsense"))
    }
  }

  test("Calling extractSubjectName with 'ART' gets back a SubjectName.ART") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("ART"))
    assert(subject === SubjectName.ART)
  }

  test("Calling extractSubjectName with 'ASSEMBLY' gets back a SubjectName.ASSEMBLY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("ASSEMBLY"))
    assert(subject === SubjectName.ASSEMBLY)
  }

  test("Calling extractSubjectName with 'EMPTY' gets back a SubjectName.EMPTY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("EMPTY"))
    assert(subject === SubjectName.EMPTY)
  }

  test("Calling extractSubjectName with 'DRAMA' gets back a SubjectName.DRAMA") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("DRAMA"))
    assert(subject === SubjectName.DRAMA)
  }

  test("Calling extractSubjectName with 'GOLDEN_TIME' gets back a SubjectName.GOLDEN_TIME") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("GOLDEN_TIME"))
    assert(subject === SubjectName.GOLDEN_TIME)
  }

  test("Calling extractSubjectName with 'HEALTH' gets back a SubjectName.HEALTH") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("HEALTH"))
    assert(subject === SubjectName.HEALTH)
  }

  test("Calling extractSubjectName with 'ICT' gets back a SubjectName.ICT") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("ICT"))
    assert(subject === SubjectName.ICT)
  }

  test("Calling extractSubjectName with 'MATHS' gets back a SubjectName.MATHS") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("MATHS"))
    assert(subject === SubjectName.MATHS)
  }

  test("Calling extractSubjectName with 'MUSIC' gets back a SubjectName.MUSIC") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("MUSIC"))
    assert(subject === SubjectName.MUSIC)
  }

  test("Calling extractSubjectName with 'NUMERACY' gets back a SubjectName.NUMERACY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("NUMERACY"))
    assert(subject === SubjectName.NUMERACY)
  }

  test("Calling extractSubjectName with 'OTHER' gets back a SubjectName.OTHER") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("OTHER"))
    assert(subject === SubjectName.OTHER)
  }

  test("Calling extractSubjectName with 'READING' gets back a SubjectName.READING") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("READING"))
    assert(subject === SubjectName.READING)
  }

  test("Calling extractSubjectName with 'PHYSICAL_EDUCATION' gets back a SubjectName.PHYSICAL_EDUCATION") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("PHYSICAL_EDUCATION"))
    assert(subject === SubjectName.PHYSICAL_EDUCATION)
  }

  test("Calling extractSubjectName with 'RME' gets back a SubjectName.RME") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("RME"))
    assert(subject === SubjectName.RME)
  }

  test("Calling extractSubjectName with 'SOFT_START' gets back a SubjectName.SOFT_START") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("SOFT_START"))
    assert(subject === SubjectName.SOFT_START)
  }

  test("Calling extractSubjectName with 'SPELLING' gets back a SubjectName.SPELLING") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("SPELLING"))
    assert(subject === SubjectName.SPELLING)
  }

  test("Calling extractSubjectName with 'TEACHER_COVERTIME' gets back a SubjectName.TEACHER_COVERTIME") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("TEACHER_COVERTIME"))
    assert(subject === SubjectName.TEACHER_COVERTIME)
  }

  test("Calling extractSubjectName with 'TOPIC' gets back a SubjectName.TOPIC") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("TOPIC"))
    assert(subject === SubjectName.TOPIC)
  }

  test("Calling extractSubjectName with 'WRITING' gets back a SubjectName.WRITING") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("WRITING"))
    assert(subject === SubjectName.WRITING)
  }

  test("Calling extractSubjectName with 'PLAY' gets back a SubjectName.PLAY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("PLAY"))
    assert(subject === SubjectName.PLAY)
  }

  test("Calling extractSubjectName with 'MODERN_LANGUAGES' gets back a SubjectName.MODERN_LANGUAGES") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("MODERN_LANGUAGES"))
    assert(subject === SubjectName.MODERN_LANGUAGES)
  }

  test("Calling extractSubjectName with 'SCIENCE' gets back a SubjectName.SCIENCE") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("SCIENCE"))
    assert(subject === SubjectName.SCIENCE)
  }

  test("Calling extractSubjectName with 'HAND_WRITING' gets back a SubjectName.HAND_WRITING") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("HAND_WRITING"))
    assert(subject === SubjectName.HAND_WRITING)
  }

  test("Calling extractSubjectName with 'GEOGRAPHY' gets back a SubjectName.GEOGRAPHY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("GEOGRAPHY"))
    assert(subject === SubjectName.GEOGRAPHY)
  }

  test("Calling extractSubjectName with 'HISTORY' gets back a SubjectName.HISTORY") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val subject = translator.extractSubjectName(BsonString("HISTORY"))
    assert(subject === SubjectName.HISTORY)
  }

  test("Calling extractAdditionalInfo with 'some value text' gets back an SubjectDetailAdditionalInfo with same value") {
    val translator = new MongoClassTimetableBsonToModelTranslator()
    val additionalInfo = translator.extractAdditionalInfo(BsonString("some value text"))
    assert(additionalInfo.value === "some value text")
  }
}
