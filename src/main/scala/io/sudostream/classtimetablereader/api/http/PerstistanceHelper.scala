package io.sudostream.classtimetablereader.api.http

import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.ClassTimetable

import scala.concurrent.Future

trait PerstistanceHelper {
  def searchForTimeTable(classNameOption: Option[String], timeToTeachUserIdOption: Option[String]): Future[Option[ClassTimetable]] = ???
}
