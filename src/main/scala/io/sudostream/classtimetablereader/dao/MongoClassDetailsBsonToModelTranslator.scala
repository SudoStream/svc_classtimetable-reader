package io.sudostream.classtimetablereader.dao

import io.sudostream.classtimetablereader.dao.mongo.ClassDetailsMongoDbSchema
import io.sudostream.timetoteach.messages.scottish.ScottishCurriculumLevel
import io.sudostream.timetoteach.messages.systemwide.model.classes._
import org.bson.BsonString
import org.mongodb.scala.{Document, bson}
import org.mongodb.scala.bson.{BsonArray, BsonString}

class MongoClassDetailsBsonToModelTranslator() {

  def convertTeachersToList(teachersWithWriteAccessArray: BsonArray): List[String] = {
    import scala.collection.JavaConversions._
    teachersWithWriteAccessArray.getValues.toList.map(_.asString().getValue)
  }

  def convertClassGroupsToList(classGroupsArray: BsonArray): List[ClassGroupsWrapper] = {
    import scala.collection.JavaConversions._
    val classGroupsListAsValues = classGroupsArray.getValues.toList
    for {
      classGroupBsonValue <- classGroupsListAsValues
      classGroupAsDoc = classGroupBsonValue.asDocument()

      groupId = classGroupAsDoc.getString(ClassDetailsMongoDbSchema.GROUP_ID).getValue
      groupName = classGroupAsDoc.getString(ClassDetailsMongoDbSchema.GROUP_NAME).getValue
      groupDescription = classGroupAsDoc.getString(ClassDetailsMongoDbSchema.GROUP_DESCRIPTION).getValue
      groupType = classGroupAsDoc.getString(ClassDetailsMongoDbSchema.GROUP_TYPE).getValue match {
        case "MATHS" => GroupType.MATHS
        case "READING" => GroupType.READING
        case "WRITING" => GroupType.WRITING
        case "SPELLING" => GroupType.SPELLING
        case _ => GroupType.OTHER
      }
      groupLevel = classGroupAsDoc.getString(ClassDetailsMongoDbSchema.GROUP_LEVEL).getValue match {
        case "EARLY" => ScottishCurriculumLevel.EARLY
        case "FIRST" => ScottishCurriculumLevel.FIRST
        case "SECOND" => ScottishCurriculumLevel.SECOND
        case "THIRD" => ScottishCurriculumLevel.THIRD
        case "FOURTH" => ScottishCurriculumLevel.FOURTH
      }

    } yield ClassGroupsWrapper(ClassGroup(
      GroupId(groupId),
      GroupName(groupName),
      GroupDescription(groupDescription),
      groupType,
      groupLevel
    ))
  }

  def translateMaybeBsonToMaybeClassTimetable(classDetailsDocumentList: List[Document]): List[ClassDetails] = {
    for {
      classDetailsDoc: Document <- classDetailsDocumentList

      maybeClassId = classDetailsDoc.get[BsonString](ClassDetailsMongoDbSchema.CLASS_ID)
      classId <- maybeClassId

      maybeSchoolId = classDetailsDoc.get[BsonString](ClassDetailsMongoDbSchema.SCHOOL_ID)
      schoolId <- maybeSchoolId

      maybeClassName = classDetailsDoc.get[BsonString](ClassDetailsMongoDbSchema.CLASS_NAME)
      className <- maybeClassName

      maybeClassDescription: Option[BsonString] = classDetailsDoc.get[BsonString](ClassDetailsMongoDbSchema.CLASS_DESCRIPTION)
      classDescription <- maybeClassDescription

      maybeTeachersWithWriteAccessArray = classDetailsDoc.get[BsonArray](ClassDetailsMongoDbSchema.TEACHERS_WITH_WRITE_ACCESS)
      teachersWithWriteAccessArray <- maybeTeachersWithWriteAccessArray
      teachersWithWriteAccessList = convertTeachersToList(teachersWithWriteAccessArray)

      maybeClassGroupsArray = classDetailsDoc.get[BsonArray](ClassDetailsMongoDbSchema.CLASS_GROUPS)
      classGroupsArray <- maybeClassGroupsArray
      classGroupsList = convertClassGroupsToList(classGroupsArray)


    } yield ClassDetails(
      ClassId(classId.getValue),
      SchoolId(schoolId.getValue),
      ClassName(className.getValue),
      ClassDescription(classDescription.getValue),
      teachersWithWriteAccessList,
      classGroupsList
    )
  }
}
