package io.sudostream.classtimetablereader.dao

import org.mongodb.scala.{Document, MongoCollection}

trait MongoDbConnectionWrapper {

  def getClassTimetableCollection: MongoCollection[Document]

}
