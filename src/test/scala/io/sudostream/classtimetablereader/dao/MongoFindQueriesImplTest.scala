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

//  test("Should find a list of 3 documents when look for classes associated with teacher id user_147ade86-39d7-46df-8ea1-012ddc344f5a") {
//    val maybeLatestClassTimetableDoc = MongoFindQueriesImpl.(Nil)
//  }

  /// Test Helpers
  def createSomeClassTimetableDocuments(): Seq[BsonDocument] = {
    BsonDocument(
      "epochMillisUTC" -> BsonNumber(10000000000L),
      "classId" -> "P3AB",
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
        "classId" -> "P3AB",
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
        "classId" -> "P3AB",
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


  def createSomeClassDetails(): Seq[BsonDocument] = {
    BsonDocument(
      "_id" -> "classId_f4eca698-f6d4-42ad-b5cb-c269a2137f70",
      "epochMillisUTC" -> BsonNumber(1516295087351L),
      "classId" -> "My New Class 1",
      "classGroups" -> BsonArray(
        BsonDocument(
          "groupId" -> "groupId_513ca5d4-d1c1-4e7f-a9af-025fe1eb68d3",
          "groupName" -> "triangles",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "EARLY"
        ),
        BsonDocument(
          "groupId" -> "groupId_c2322b92-88bb-46c7-8a88-e8c11656e78a",
          "groupName" -> "squares",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "FIRST"
        ),
        BsonDocument(
          "groupId" -> "groupId_01d9e731-cc30-405f-b090-263ec9ecafe3",
          "groupName" -> "circles",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "FIRST"
        )
      ),
      "teachersWithWriteAccess" -> BsonArray(
        "user_147ade86-39d7-46df-8ea1-012ddc344f5a"
      )
    ) :: BsonDocument(
      "_id" -> "classId_f4eca698-f6d4-42ad-b5cb-c269a2137f71",
      "epochMillisUTC" -> BsonNumber(1516295088358L),
      "classId" -> "My New Class 2",
      "classGroups" -> BsonArray(
        BsonDocument(
          "groupId" -> "groupId_513ca5d4-d1c1-4e7f-a9af-025fe1eb68a1",
          "groupName" -> "shakeys",
          "groupType" -> "LITERACY",
          "groupLevel" -> "EARLY"
        ),
        BsonDocument(
          "groupId" -> "groupId_c2322b92-88bb-46c7-8a88-e8c11656e7b1",
          "groupName" -> "clarks",
          "groupType" -> "LITERACY",
          "groupLevel" -> "FIRST"
        ),
        BsonDocument(
          "groupId" -> "groupId_01d9e731-cc30-405f-b090-263ec9ecafc1",
          "groupName" -> "yeates",
          "groupType" -> "LITERACY",
          "groupLevel" -> "FIRST"
        )
      ),
      "teachersWithWriteAccess" -> BsonArray(
        "user_147ade86-39d7-46df-8ea1-012ddc344f5a"
      )
    ) :: BsonDocument(
      "_id" -> "classId_f4eca698-f6d4-42ad-b5cb-c269a2137f72",
      "epochMillisUTC" -> BsonNumber(1516295089359L),
      "classId" -> "My New Class 3",
      "classGroups" -> BsonArray(
        BsonDocument(
          "groupId" -> "groupId_513ca5d4-d1c1-4e7f-a9af-025fe1eb68t1",
          "groupName" -> "blues",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "EARLY"
        ),
        BsonDocument(
          "groupId" -> "groupId_c2322b92-88bb-46c7-8a88-e8c11656e78u1",
          "groupName" -> "reds",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "FIRST"
        ),
        BsonDocument(
          "groupId" -> "groupId_01d9e731-cc30-405f-b090-263ec9ecafi1",
          "groupName" -> "yellows",
          "groupType" -> "MATHEMATICS",
          "groupLevel" -> "FIRST"
        )
      ),
      "teachersWithWriteAccess" -> BsonArray(
        "user_147ade86-39d7-46df-8ea1-012ddc344f5a"
      )
    ) :: Nil
  }

}
