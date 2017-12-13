package io.sudostream.classtimetablereader.dao.mongo

import org.mongodb.scala.{Document, MongoCollection}

trait MongoDbConnectionWrapper {

  def getClassTimetableCollection: MongoCollection[Document]

}
