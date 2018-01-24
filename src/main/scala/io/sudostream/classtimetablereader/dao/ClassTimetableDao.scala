package io.sudostream.classtimetablereader.dao

import io.sudostream.classtimetablereader.model.ClassId
import io.sudostream.timetoteach.messages.systemwide.model.classes.ClassDetails
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.{ClassTimetable, TimeToTeachId}

import scala.concurrent.Future

trait ClassTimetableDao {

  def findClassTimetable(classId: ClassId, timeToTeachId: TimeToTeachId): Future[Option[ClassTimetable]]

  def findTeachersClasses(timeToTeachId: TimeToTeachId): Future[List[ClassDetails]]

}
