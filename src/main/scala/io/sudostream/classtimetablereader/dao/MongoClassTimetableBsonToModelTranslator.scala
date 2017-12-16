package io.sudostream.classtimetablereader.dao

import java.time.LocalTime

import io.sudostream.classtimetablereader.dao.mongo.ClassTimetableMongoDbSchema
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.sessions._
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.subjectdetail.{SubjectDetail, SubjectDetailAdditionalInfo, SubjectDetailWrapper, SubjectName}
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.time.{ClassTimetableSchoolTimes, DayOfTheWeek, EndTime, StartTime}
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.{ClassName, ClassTimetable, TimeToTeachId}
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonString}

class MongoClassTimetableBsonToModelTranslator() {

  def translateMaybeBsonToMaybeClassTimetable(timeToTeachId: TimeToTeachId,
                                              maybeClassTimetableBson: Option[BsonDocument]): Option[ClassTimetable] = {
    maybeClassTimetableBson match {
      case Some(classTimetableBson) => translateBsonToMaybeClassTimetable(timeToTeachId, classTimetableBson)
      case None => None
    }
  }


  private[dao] def translateBsonToMaybeClassTimetable(timeToTeachId: TimeToTeachId,
                                                      classTimetableBson: BsonDocument): Option[ClassTimetable] = {

    println(s"translateBsonToMaybeClassTimetable() - classTimetableBson : ${classTimetableBson.toString} ")

    if (classTimetableBson.isEmpty) None
    else {
      val epochMillisUTC = classTimetableBson.getNumber(ClassTimetableMongoDbSchema.EPOCH_MILLI_UTC)
      val className = classTimetableBson.getString(ClassTimetableMongoDbSchema.CLASS_NAME)
      val classTimetablesForSpecificClassAsDoc = classTimetableBson.getDocument(
        ClassTimetableMongoDbSchema.CLASS_TIMETABLES_FOR_SPECIFIC_CLASS)
      val schoolTimes = createSchoolTimesFromDoc(classTimetablesForSpecificClassAsDoc.getArray(ClassTimetableMongoDbSchema.SCHOOL_TIMES))
      val allSessionsOfTheWeek = createAllSessionsOfTheWeek(classTimetablesForSpecificClassAsDoc.getArray(ClassTimetableMongoDbSchema.ALL_SESSIONS_OF_THE_WEEK))
      Some(ClassTimetable(
        timeToTeachId,
        ClassName(className.getValue),
        schoolTimes,
        allSessionsOfTheWeek
      ))
    }
  }

  private[dao] def createSchoolTimesFromDoc(schoolTimesAsArray: BsonArray): ClassTimetableSchoolTimes = {
    import scala.collection.JavaConversions._

    val schoolBoundaryWrappers = {
      for {
        schoolTimeAsValue <- schoolTimesAsArray
        schoolTimeAsDoc = schoolTimeAsValue.asInstanceOf[BsonDocument]
        sessionBoundaryName = schoolTimeAsDoc.getString(ClassTimetableMongoDbSchema.SESSION_BOUNDARY_NAME).getValue
        boundaryStartTime = schoolTimeAsDoc.getString(ClassTimetableMongoDbSchema.SESSION_BOUNDARY_START_TIME).getValue
        boundaryType = extractBoundaryType(schoolTimeAsDoc.getString(ClassTimetableMongoDbSchema.SESSION_BOUNDARY_TYPE))
        sessionName = extractSessionName(schoolTimeAsDoc.getString(ClassTimetableMongoDbSchema.SESSION_BOUNDARY_SESSION_NAME))
      } yield SessionBoundaryWrapper(SessionBoundary(
        SessionBoundaryName(sessionBoundaryName),
        StartTime(boundaryStartTime),
        boundaryType,
        sessionName
      ))
    }.toList

    ClassTimetableSchoolTimes(schoolBoundaryWrappers)
  }

  private[dao] def extractBoundaryType(boundaryValue: BsonString): SessionBoundaryType = {
    boundaryValue.getValue match {
      case "START_OF_TEACHING_SESSION" => SessionBoundaryType.START_OF_TEACHING_SESSION
      case "END_OF_TEACHING_SESSION" => SessionBoundaryType.END_OF_TEACHING_SESSION
      case _ => SessionBoundaryType.START_OF_TEACHING_SESSION
    }
  }

  private[dao] def extractSessionName(sessionValue: BsonString): Option[SessionName] = {
    val value = sessionValue.getValue
    if (value.isEmpty) None else Some(SessionName(value))
  }

  private[dao] def createAllSessionsOfTheWeek(allSessionsAsArray: BsonArray): scala.List[SessionOfTheDayWrapper] = {
    import scala.collection.JavaConversions._

    {
      for {
        sessionOfTheWeekValue <- allSessionsAsArray
        sessionOfTheWeekDoc = sessionOfTheWeekValue.asInstanceOf[BsonDocument]
        sessionName = sessionOfTheWeekDoc.getString(ClassTimetableMongoDbSchema.SESSIONS_WEEK_SESSION_NAME)
        dayOfTheWeek = extractDayOfTheWeek(sessionOfTheWeekDoc.getString(ClassTimetableMongoDbSchema.SESSIONS_WEEK_DAY_OF_THE_WEEK))
        startTime = extractStartTime(sessionOfTheWeekDoc.getString(ClassTimetableMongoDbSchema.SESSIONS_WEEK_START_TIME))
        endTime = extractEndTime(sessionOfTheWeekDoc.getString(ClassTimetableMongoDbSchema.SESSIONS_WEEK_END_TIME))
        subjects = extractSubjects(sessionOfTheWeekDoc.getArray(ClassTimetableMongoDbSchema.SESSIONS_WEEK_SUBJECTS))
      } yield SessionOfTheDayWrapper(SessionOfTheDay(
        SessionName(sessionName.getValue),
        dayOfTheWeek,
        startTime,
        endTime,
        subjects
      ))
    }.toList
  }

  private[dao] def extractDayOfTheWeek(dayOfTheWeek: BsonString): DayOfTheWeek = {
    dayOfTheWeek.getValue.toUpperCase.trim match {
      case "MONDAY" => DayOfTheWeek.MONDAY
      case "TUESDAY" => DayOfTheWeek.TUESDAY
      case "WEDNESDAY" => DayOfTheWeek.WEDNESDAY
      case "THURSDAY" => DayOfTheWeek.THURSDAY
      case "FRIDAY" => DayOfTheWeek.FRIDAY
    }
  }

  private[dao] def extractStartTime(timeIso8601: BsonString): StartTime = {
    LocalTime.parse(timeIso8601.getValue)
    StartTime(timeIso8601.getValue)
  }

  private[dao] def extractEndTime(timeIso8601: BsonString): EndTime = {
    LocalTime.parse(timeIso8601.getValue)
    EndTime(timeIso8601.getValue)
  }

  private[dao] def extractSubjectName(subjectBson: BsonString): SubjectName = {
    subjectBson.getValue.toUpperCase.trim match {
      case "ART" => SubjectName.ART
      case "ASSEMBLY" => SubjectName.ASSEMBLY
      case "EMPTY" => SubjectName.EMPTY
      case "DRAMA" => SubjectName.DRAMA
      case "GOLDEN_TIME" => SubjectName.GOLDEN_TIME
      case "HEALTH" => SubjectName.HEALTH
      case "ICT" => SubjectName.ICT
      case "MATHS" => SubjectName.MATHS
      case "MUSIC" => SubjectName.MUSIC
      case "NUMERACY" => SubjectName.NUMERACY
      case "OTHER" => SubjectName.OTHER
      case "READING" => SubjectName.READING
      case "PHYSICAL_EDUCATION" => SubjectName.PHYSICAL_EDUCATION
      case "RME" => SubjectName.RME
      case "SOFT_START" => SubjectName.SOFT_START
      case "SPELLING" => SubjectName.SPELLING
      case "TEACHER_COVERTIME" => SubjectName.TEACHER_COVERTIME
      case "TOPIC" => SubjectName.TOPIC
      case "WRITING" => SubjectName.WRITING
      case "PLAY" => SubjectName.PLAY
      case "MODERN_LANGUAGES" => SubjectName.MODERN_LANGUAGES
      case "SCIENCE" => SubjectName.SCIENCE
      case "HAND_WRITING" => SubjectName.HAND_WRITING
      case "GEOGRAPHY" => SubjectName.GEOGRAPHY
      case "HISTORY" => SubjectName.HISTORY
      case otherSubjectValue => throw new RuntimeException(s"Did not recognise subject '$otherSubjectValue'")
    }
  }

  private[dao] def extractAdditionalInfo(additionalInfo: BsonString): SubjectDetailAdditionalInfo = {
    SubjectDetailAdditionalInfo(additionalInfo.getValue)
  }

  private[dao] def extractSubjects(subjectsArray: BsonArray): List[SubjectDetailWrapper] = {
    import scala.collection.JavaConversions._
    {
      for {
        subjectDetailValue <- subjectsArray
        subjectDetailDoc = subjectDetailValue.asInstanceOf[BsonDocument]
        subjectName = extractSubjectName(subjectDetailDoc.getString(ClassTimetableMongoDbSchema.SUBJECT_DETAIL_NAME))
        startTime = extractStartTime(subjectDetailDoc.getString(ClassTimetableMongoDbSchema.SUBJECT_DETAIL_START_TIME))
        endTime = extractEndTime(subjectDetailDoc.getString(ClassTimetableMongoDbSchema.SUBJECT_DETAIL_END_TIME))
        additionalInfo = extractAdditionalInfo(subjectDetailDoc.getString(ClassTimetableMongoDbSchema.SUBJECT_DETAIL_ADDITIONAL_INFO))
      } yield SubjectDetailWrapper(SubjectDetail(
        subjectName,
        startTime,
        endTime,
        additionalInfo
      ))
    }.toList
  }
}
