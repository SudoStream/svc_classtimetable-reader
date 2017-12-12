package io.sudostream.classtimetablereader.dao

import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonNumber, BsonString}

trait MongoClassTimetableBsonToModelTranslatorTestHelper {

  def createValidClassTimetableBson = {
    BsonDocument(
      "_id" -> BsonString("user12345"),
      "allUserClassTimetables" -> BsonArray(
        BsonDocument(
          "epochMillisUTC" -> BsonNumber(1513093735330L),
          "className" -> BsonString("P3AB"),
          "classTimetablesForSpecificClass" -> BsonDocument(
            "schoolTimes" -> createSchoolTimesArray(),
            "allSessionsOfTheWeek" -> createAllSessionsOfTheWeekArray()
          )
        )
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


  def createAllSessionsOfTheWeekArray(): BsonArray = BsonArray()


}
