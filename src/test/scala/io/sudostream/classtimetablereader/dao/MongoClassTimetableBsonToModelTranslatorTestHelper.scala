package io.sudostream.classtimetablereader.dao

import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonNumber, BsonString}

trait MongoClassTimetableBsonToModelTranslatorTestHelper {

  def createValidClassTimetableBson(): BsonDocument = {
    BsonDocument(
      "epochMillisUTC" -> BsonNumber(1513093735330L),
      "className" -> BsonString("P3AB"),
      "classTimetablesForSpecificClass" -> BsonDocument(
        "schoolTimes" -> createSchoolTimesArray(),
        "allSessionsOfTheWeek" -> createAllSessionsOfTheWeekArray()
      )
    )
  }

  def createSchoolTimesArray(): BsonArray = {
    BsonArray(
      BsonDocument(
        "sessionBoundaryName" -> BsonString("lunch-starts"),
        "boundaryStartTime" -> BsonString("12:00"),
        "boundaryType" -> BsonString("END_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("")
      ),
      BsonDocument(
        "sessionBoundaryName" -> BsonString("lunch-ends"),
        "boundaryStartTime" -> BsonString("13:00"),
        "boundaryType" -> BsonString("START_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("AfternoonSession")
      ),
      BsonDocument(
        "sessionBoundaryName" -> BsonString("morning-break-ends"),
        "boundaryStartTime" -> BsonString("10:45"),
        "boundaryType" -> BsonString("START_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("LateMorningSession")
      ),
      BsonDocument(
        "sessionBoundaryName" -> BsonString("morning-break-starts"),
        "boundaryStartTime" -> BsonString("10:30"),
        "boundaryType" -> BsonString("END_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("")

      ),
      BsonDocument(
        "sessionBoundaryName" -> BsonString("school-day-ends"),
        "boundaryStartTime" -> BsonString("15:00"),
        "boundaryType" -> BsonString("END_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("")

      ),
      BsonDocument(
        "sessionBoundaryName" -> BsonString("school-day-starts"),
        "boundaryStartTime" -> BsonString("09:00"),
        "boundaryType" -> BsonString("START_OF_TEACHING_SESSION"),
        "sessionName" -> BsonString("EarlyMorningSession")
      )
    )
  }


  def createAllSessionsOfTheWeekArray(): BsonArray = {
    BsonArray(
      BsonDocument(
        "sessionName" -> "early-morning-session",
        "dayOfTheWeek" -> "WEDNESDAY",
        "startTime" -> "09:00",
        "endTime" -> "10:30",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> BsonString("EMPTY"),
            "startTime" -> BsonString("09:00"),
            "endTime" -> BsonString("10:30"),
            "additionalInfo" -> BsonString("")
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "late-morning-session",
        "dayOfTheWeek" -> "WEDNESDAY",
        "startTime" -> "10:45",
        "endTime" -> "12:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "10:45",
            "endTime" -> "12:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "afternoon-session",
        "dayOfTheWeek" -> "WEDNESDAY",
        "startTime" -> "13:00",
        "endTime" -> "15:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "13:00",
            "endTime" -> "15:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "early-morning-session",
        "dayOfTheWeek" -> "THURSDAY",
        "startTime" -> "09:00",
        "endTime" -> "10:30",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "09:00",
            "endTime" -> "10:30",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "late-morning-session",
        "dayOfTheWeek" -> "THURSDAY",
        "startTime" -> "10:45",
        "endTime" -> "12:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "10:45",
            "endTime" -> "12:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "afternoon-session",
        "dayOfTheWeek" -> "THURSDAY",
        "startTime" -> "13:00",
        "endTime" -> "15:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "13:00",
            "endTime" -> "15:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "early-morning-session",
        "dayOfTheWeek" -> "TUESDAY",
        "startTime" -> "09:00",
        "endTime" -> "10:30",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "09:00",
            "endTime" -> "10:30",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "late-morning-session",
        "dayOfTheWeek" -> "TUESDAY",
        "startTime" -> "10:45",
        "endTime" -> "12:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "10:45",
            "endTime" -> "12:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "afternoon-session",
        "dayOfTheWeek" -> "TUESDAY",
        "startTime" -> "13:00",
        "endTime" -> "15:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "13:00",
            "endTime" -> "15:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "early-morning-session",
        "dayOfTheWeek" -> "MONDAY",
        "startTime" -> "09:00",
        "endTime" -> "10:30",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "ICT",
            "startTime" -> "09:00",
            "endTime" -> "10:30",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "late-morning-session",
        "dayOfTheWeek" -> "MONDAY",
        "startTime" -> "10:45",
        "endTime" -> "12:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "ART",
            "startTime" -> "10:45",
            "endTime" -> "11:10",
            "additionalInfo" -> ""
          ),
          BsonDocument(
            "subjectName" -> "MATHS",
            "startTime" -> "11:10",
            "endTime" -> "12:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "afternoon-session",
        "dayOfTheWeek" -> "MONDAY",
        "startTime" -> "13:00",
        "endTime" -> "15:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "GEOGRAPHY",
            "startTime" -> "13:00",
            "endTime" -> "13:40",
            "additionalInfo" -> ""
          ),
          BsonDocument(
            "subjectName" -> "SPELLING",
            "startTime" -> "13:40",
            "endTime" -> "15:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "early-morning-session",
        "dayOfTheWeek" -> "FRIDAY",
        "startTime" -> "09:00",
        "endTime" -> "10:30",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "09:00",
            "endTime" -> "10:30",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "late-morning-session",
        "dayOfTheWeek" -> "FRIDAY",
        "startTime" -> "10:45",
        "endTime" -> "12:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "10:45",
            "endTime" -> "12:00",
            "additionalInfo" -> ""
          )
        )
      ),
      BsonDocument(
        "sessionName" -> "afternoon-session",
        "dayOfTheWeek" -> "FRIDAY",
        "startTime" -> "13:00",
        "endTime" -> "15:00",
        "subjects" -> BsonArray(
          BsonDocument(
            "subjectName" -> "EMPTY",
            "startTime" -> "13:00",
            "endTime" -> "15:00",
            "additionalInfo" -> ""
          )
        )
      )
    )
  }

  def createSomeSubjects(): BsonArray = {
    BsonArray(
      BsonDocument(
        "subjectName" -> "GEOGRAPHY",
        "startTime" -> "13:00",
        "endTime" -> "13:40",
        "additionalInfo" -> ""
      ),
      BsonDocument(
        "subjectName" -> "SPELLING",
        "startTime" -> "13:40",
        "endTime" -> "15:00",
        "additionalInfo" -> ""
      )
    )
  }
}
