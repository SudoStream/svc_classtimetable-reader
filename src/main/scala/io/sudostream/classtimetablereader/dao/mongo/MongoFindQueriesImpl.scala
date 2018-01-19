package io.sudostream.classtimetablereader.dao.mongo

import io.sudostream.classtimetablereader.model.ClassName
import io.sudostream.timetoteach.messages.systemwide.model.classtimetable.TimeToTeachId
import org.bson.BsonValue
import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonString}
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

    println(s"findClassTimetable() - ClassName: ${className.value}, TimeToTeachId : ${timeToTeachId.value}")

    val findMatcher = Document("_id" -> timeToTeachId.value)
    println(s"findClassTimetable() - findMatcher : ${findMatcher.toString()}")
    val classTimetablesMongoDocuments: FindObservable[Document] = classTimetableCollection.find(findMatcher)
    val futureClassTimetablesMongoDocuments = classTimetablesMongoDocuments.toFuture

    futureClassTimetablesMongoDocuments.map {
      classTimetablesMongoDocuments =>
        if (classTimetablesMongoDocuments.size != 1) {
          println("findClassTimetable()  Oh dear ... didn't find what we were looking for")
          None
        } else {
          println("findClassTimetable()  We did fine something :-)")
          val classTimetablesMongoDocumentsList = classTimetablesMongoDocuments.toList
          println(s"findClassTimetable() - classTimetablesMongoDocumentsList : ${classTimetablesMongoDocumentsList.toString()}")
          val classTimetableValuesHistory = {
            for {
              classTimetableDoc <- classTimetablesMongoDocumentsList
              maybeAllClassTimetables = classTimetableDoc.get[BsonArray](ClassTimetableMongoDbSchema.ALL_USER_CLASS_TIMETABLES)
              allClassTimetables <- maybeAllClassTimetables
              classTimetableValues = allClassTimetables.getValues

            } yield classTimetableValues
          }.flatten

          println(s"findClassTimetable() - classTimetableValuesHistory : ${classTimetableValuesHistory.toString()}")

          val classTimetableHistory = for {
            classTimetableValue: BsonValue <- classTimetableValuesHistory
            classTimetableDoc = classTimetableValue.asDocument()
            theClassName = classTimetableDoc.getString("className").getValue
            if theClassName == className.value
          } yield classTimetableDoc

          println(s"findClassTimetable() - classTimetableHistory : ${classTimetableHistory.toString()}")

          val maybeClassTimetable = MongoFindQueriesImpl.extractLatestClassTimetable(classTimetableHistory)
          println(s"findClassTimetable() - maybeClassTimetable : ${maybeClassTimetable.toString}")
          maybeClassTimetable
        }
    }
  }


  ///////////////////////////////////

  val classesCollection: MongoCollection[Document] = mongoDbConnectionWrapper.getClassesCollection

  override def findTeachersClasses(timeToTeachId: TimeToTeachId): Future[List[Document]] = {
    println(s"findTeachersClasses() - TimeToTeachId : ${timeToTeachId.value}")

    val findMatcher = Document(
      "$and" -> BsonArray(
        Document("teachersWithWriteAccess" -> timeToTeachId.value),
        Document("teachersWhoDeletedClass" ->
          Document("$nin" ->
            BsonArray(
              timeToTeachId.value
            )
          )
        )
      )
    )

    println(s"findTeachersClasses() - findMatcher : ${findMatcher.toString()}")
    val classDetailsMongoDocuments: FindObservable[Document] = classesCollection.find(findMatcher)
    val futureClassDetailsMongoDocuments = classDetailsMongoDocuments.toFuture

    futureClassDetailsMongoDocuments.map {
      classDetailsMongoDocuments =>
        classDetailsMongoDocuments.toList
    }
  }
}
