package io.sudostream.classtimetablereader.dao.mongo

import java.net.URI
import javax.net.ssl.{HostnameVerifier, SSLSession}

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.stream.Materializer
import com.mongodb.connection.ClusterSettings
import com.typesafe.config.ConfigFactory
import io.sudostream.classtimetablereader.Main
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.connection.{NettyStreamFactoryFactory, SslSettings}
import org.mongodb.scala.{Document, MongoClient, MongoClientSettings, MongoCollection, MongoDatabase, ServerAddress}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

sealed class MongoDbConnectionWrapperImpl(actorSystemWrapper: ActorSystemWrapper) extends MongoDbConnectionWrapper {

  implicit val system: ActorSystem = actorSystemWrapper.system
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = actorSystemWrapper.materializer
  val log: LoggingAdapter = system.log

  private val config = ConfigFactory.load()
  private val mongoDbUriString = config.getString("mongodb.connection_uri")
  private val mongoDbUri = new URI(mongoDbUriString)
  private val classTimetableDatabaseName = config.getString("classtimetable-reader-service.database_name")
  private val classTimetableCollectionName = config.getString("classtimetable-reader-service.classtimetables_collection")
  private val classesCollectionName = config.getString("classtimetable-reader-service.classes_collection")

  private val isLocalMongoDb: Boolean = try {
    if (sys.env("LOCAL_MONGO_DB") == "true") true else false
  } catch {
    case e: Exception => false
  }

  log.info(s"Running Local = $isLocalMongoDb")

  def createMongoClient: MongoClient = {
    if (isLocalMongoDb || Main.isMinikubeRun) {
      buildLocalMongoDbClient
    } else {
      log.info(s"connecting to mongo db at '${mongoDbUri.getHost}:${mongoDbUri.getPort}'")
      System.setProperty("org.mongodb.async.type", "netty")
      MongoClient(mongoDbUriString)
    }
  }

  def getClassTimetableCollection: MongoCollection[Document] = {
    val database: MongoDatabase = createMongoClient.getDatabase(classTimetableDatabaseName)
    database.getCollection(classTimetableCollectionName)
  }

  private def buildLocalMongoDbClient = {
    val mongoKeystorePassword = try {
      sys.env("MONGODB_KEYSTORE_PASSWORD")
    } catch {
      case e: Exception => ""
    }

    val mongoDbHost = mongoDbUri.getHost
    val mongoDbPort = mongoDbUri.getPort
    println(s"mongo host = '$mongoDbHost'")
    println(s"mongo port = '$mongoDbPort'")

    val clusterSettings: ClusterSettings = ClusterSettings.builder().hosts(
      List(new ServerAddress(mongoDbHost, mongoDbPort)).asJava).build()

    val mongoSslClientSettings = MongoClientSettings.builder()
      .sslSettings(SslSettings.builder()
        .enabled(true)
        .invalidHostNameAllowed(true)
        .build())
      .streamFactoryFactory(NettyStreamFactoryFactory())
      .clusterSettings(clusterSettings)
      .build()

    MongoClient(mongoSslClientSettings)
  }

  override def getClassesCollection: MongoCollection[Document] = {
    val database: MongoDatabase = createMongoClient.getDatabase(classTimetableDatabaseName)
    database.getCollection(classesCollectionName)
  }

  override def ensureIndexes(): Unit = {
    val teachersWithWriteAccessIndex = BsonDocument(ClassDetailsMongoDbSchema.TEACHERS_WITH_WRITE_ACCESS -> 1)
    log.info(s"Ensuring index created : ${teachersWithWriteAccessIndex.toString}")
    val obs = getClassesCollection.createIndex(teachersWithWriteAccessIndex)
    obs.toFuture().onComplete {
      case Success(msg) => log.info(s"Ensure index attempt completed with msg : $msg")
      case Failure(ex) => log.info(s"Ensure index failed to complete: ${ex.getMessage}")
    }

  }
}

class AcceptAllHostNameVerifier extends HostnameVerifier {
  override def verify(s: String, sslSession: SSLSession) = true
}
