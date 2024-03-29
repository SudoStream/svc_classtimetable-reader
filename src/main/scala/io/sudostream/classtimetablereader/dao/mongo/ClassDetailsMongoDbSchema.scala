package io.sudostream.classtimetablereader.dao.mongo

object ClassDetailsMongoDbSchema {
  val CLASS_ID = "_id"
  val SCHOOL_ID = "schoolId"
  val EPOCH_MILLI_UTC = "epochMillisUTC"
  val CLASS_NAME = "className"
  val CLASS_DESCRIPTION = "classDescription"
  val CLASS_GROUPS = "classGroups"
  val TEACHERS_WITH_WRITE_ACCESS = "teachersWithWriteAccess"

  //  val AUDIT_LOG = "audit_log"

  val GROUP_ID = "groupId"
  val GROUP_NAME = "groupName"
  val GROUP_DESCRIPTION = "groupDescription"
  val GROUP_TYPE = "groupType"
  val GROUP_LEVEL = "groupLevel"
}
