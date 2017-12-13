package io.sudostream.classtimetablereader.dao.mongo

import io.sudostream.classtimetablereader.model.ClassName
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import org.bson.BsonValue
import org.mongodb.scala.bson.{BsonArray, BsonDocument}
import org.mongodb.scala.{Document, FindObservable, MongoCollection}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MongoFindQueriesImpl {
  private[dao] def extractLatestClassTimetable(classTimetableHistory: Seq[BsonDocument]): Option[BsonDocument] = {
    if (classTimetableHistory.isEmpty) None
    else {
      @tailrec
      def loop(currentLatestTimetable: BsonDocument, remainingDocsToCheck: Seq[BsonDocument]): Option[BsonDocument] = {
        if (remainingDocsToCheck.isEmpty) Some(currentLatestTimetable)
        else {
          val nextTimetableToCheck = remainingDocsToCheck.head
          val nextEpochMilliUtc = nextTimetableToCheck.getNumber("epochMillisUTC").longValue()
          val nextLatestTimetable = if (nextEpochMilliUtc > currentLatestTimetable.getNumber("epochMillisUTC").longValue()) {
            nextTimetableToCheck
          } else {
            currentLatestTimetable
          }
          loop(nextLatestTimetable, remainingDocsToCheck.tail)
        }
      }

      loop(classTimetableHistory.head, classTimetableHistory.tail)
    }
  }
}

class MongoFindQueriesImpl(mongoDbConnectionWrapper: MongoDbConnectionWrapper) extends MongoFindQueriesProxy {

  val classTimetableCollection: MongoCollection[Document] = mongoDbConnectionWrapper.getClassTimetableCollection


  override def findClassTimetable(className: ClassName, timeToTeachId: TimeToTeachId): Future[Option[BsonDocument]] = {
    import scala.collection.JavaConversions._

    val findMatcher = Document("_id" -> timeToTeachId.value)
    val classTimetablesMongoDocuments: FindObservable[Document] = classTimetableCollection.find(findMatcher)
    val futureClassTimetablesMongoDocuments = classTimetablesMongoDocuments.toFuture

    futureClassTimetablesMongoDocuments.map {
      classTimetablesMongoDocuments =>
        if (classTimetablesMongoDocuments.size != 1) None
        else {
          val classTimetablesMongoDocumentsList = classTimetablesMongoDocuments.toList
          val classTimetableValuesHistory = {
            for {
              classTimetableDoc <- classTimetablesMongoDocumentsList
              maybeAllClassTimetables = classTimetableDoc.get[BsonArray]("classTimetables")
              allClassTimetables <- maybeAllClassTimetables
              classTimetableValues = allClassTimetables.getValues

            } yield classTimetableValues
          }.flatten

          val classTimetableHistory = for {
            classTimetableValue: BsonValue <- classTimetableValuesHistory
            classTimetableDoc = classTimetableValue.asDocument()
            theClassName = classTimetableDoc.getString("className").getValue
            if theClassName == className.value
          } yield classTimetableDoc

          MongoFindQueriesImpl.extractLatestClassTimetable(classTimetableHistory)
        }
    }
  }
}
