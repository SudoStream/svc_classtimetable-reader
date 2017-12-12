package io.sudostream.classtimetablereader.dao


import io.sudostream.classtimetablereader.dao.mongo.MongoFindQueriesImpl
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonNumber}
import org.scalatest.FunSuite

class MongoFindQueriesImplTest extends FunSuite {

  test("Extracting the latest document of valid docs is defined") {
    val maybeLatestClassTimetableDoc = MongoFindQueriesImpl.extractLatestClassTimetable(createSomeClassTimetableDocuments())
    assert(maybeLatestClassTimetableDoc.isDefined)
  }

  test("Extracting the latest document of valid docs is 10000000004") {
    val maybeLatestClassTimetableDoc = MongoFindQueriesImpl.extractLatestClassTimetable(createSomeClassTimetableDocuments())
    assert(maybeLatestClassTimetableDoc.get.getNumber("epochMillisUTC").longValue() === 10000000004L)
  }

  test("Extracting the latest document of empty list is None") {
    val maybeLatestClassTimetableDoc = MongoFindQueriesImpl.extractLatestClassTimetable(Nil)
    assert(maybeLatestClassTimetableDoc.isEmpty)
  }


  /// Test Helpers
  def createSomeClassTimetableDocuments(): Seq[BsonDocument] = {
    BsonDocument(
      "epochMillisUTC" -> BsonNumber(10000000000L),
      "className" -> "P3AB",
      "classTimetables" -> BsonDocument(
        "schoolTimes" -> BsonArray(
          BsonDocument(
            "sessionBoundaryName" -> "lunch-starts",
            "boundaryStartTime" -> "12:00",
            "boundaryType" -> "END_OF_TEACHING_SESSION",
            "sessionName" -> ""
          ),
          BsonDocument(
            "sessionBoundaryName" -> "lunch-ends",
            "boundaryStartTime" -> "13:00",
            "boundaryType" -> "START_OF_TEACHING_SESSION",
            "sessionName" -> "AfternoonSession"
          ),
          BsonDocument(
            "sessionBoundaryName" -> "morning-break-ends",
            "boundaryStartTime" -> "10:45",
            "boundaryType" -> "START_OF_TEACHING_SESSION",
            "sessionName" -> "LateMorningSession"
          ),
          BsonDocument(
            "sessionBoundaryName" -> "morning-break-starts",
            "boundaryStartTime" -> "10:30",
            "boundaryType" -> "END_OF_TEACHING_SESSION",
            "sessionName" -> ""
          ),
          BsonDocument(
            "sessionBoundaryName" -> "school-day-ends",
            "boundaryStartTime" -> "15:00",
            "boundaryType" -> "END_OF_TEACHING_SESSION",
            "sessionName" -> ""
          ),
          BsonDocument(
            "sessionBoundaryName" -> "school-day-starts",
            "boundaryStartTime" -> "09:00",
            "boundaryType" -> "START_OF_TEACHING_SESSION",
            "sessionName" -> "EarlyMorningSession"
          )
        )
      )
    ) ::
      BsonDocument(
        "epochMillisUTC" -> BsonNumber(10000000004L),
        "className" -> "P3AB",
        "classTimetables" -> BsonDocument(
          "schoolTimes" -> BsonArray(
            BsonDocument(
              "sessionBoundaryName" -> "lunch-starts",
              "boundaryStartTime" -> "12:00",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "lunch-ends",
              "boundaryStartTime" -> "13:00",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "AfternoonSession"
            ),
            BsonDocument(
              "sessionBoundaryName" -> "morning-break-ends",
              "boundaryStartTime" -> "10:45",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "LateMorningSession"
            ),
            BsonDocument(
              "sessionBoundaryName" -> "morning-break-starts",
              "boundaryStartTime" -> "10:30",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "school-day-ends",
              "boundaryStartTime" -> "15:00",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "school-day-starts",
              "boundaryStartTime" -> "09:00",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "EarlyMorningSession"
            )
          )
        )
      ) ::
      BsonDocument(
        "epochMillisUTC" -> BsonNumber(10000000003L),
        "className" -> "P3AB",
        "classTimetables" -> BsonDocument(
          "schoolTimes" -> BsonArray(
            BsonDocument(
              "sessionBoundaryName" -> "lunch-starts",
              "boundaryStartTime" -> "12:00",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "lunch-ends",
              "boundaryStartTime" -> "13:00",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "AfternoonSession"
            ),
            BsonDocument(
              "sessionBoundaryName" -> "morning-break-ends",
              "boundaryStartTime" -> "10:45",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "LateMorningSession"
            ),
            BsonDocument(
              "sessionBoundaryName" -> "morning-break-starts",
              "boundaryStartTime" -> "10:30",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "school-day-ends",
              "boundaryStartTime" -> "15:00",
              "boundaryType" -> "END_OF_TEACHING_SESSION",
              "sessionName" -> ""
            ),
            BsonDocument(
              "sessionBoundaryName" -> "school-day-starts",
              "boundaryStartTime" -> "09:00",
              "boundaryType" -> "START_OF_TEACHING_SESSION",
              "sessionName" -> "EarlyMorningSession"
            )
          )
        )
      ) :: Nil
  }

}
