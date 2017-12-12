package io.sudostream.classtimetablereader.dao

import io.sudostream.classtimetablereader.model.{ClassName, TimeToTeachId}
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.ClassTimetable

import scala.concurrent.Future

trait ClassTimetableDao {

  def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[ClassTimetable]]

}
