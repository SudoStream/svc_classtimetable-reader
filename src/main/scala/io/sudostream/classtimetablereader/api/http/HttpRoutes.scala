package io.sudostream.classtimetablereader.api.http

import java.io.ByteArrayOutputStream
import java.time.Instant
import java.util.UUID

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.kafka.scaladsl.Producer
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import akka.util.Timeout
import io.sudostream.timetoteach.messages.events.SystemEvent
import io.sudostream.timetoteach.messages.systemwide.model.{SocialNetwork, User}
import io.sudostream.timetoteach.messages.systemwide.{SystemEventType, TimeToTeachApplication}
import io.sudostream.classtimetablereader.api.kafka.StreamingComponents
import io.sudostream.classtimetablereader.config.ActorSystemWrapper
import io.sudostream.classtimetablereader.dao.UserReaderDao
import org.apache.avro.io.{DatumWriter, EncoderFactory}
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.kafka.clients.producer.ProducerRecord
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class HttpRoutes(dao: UserReaderDao,
                 actorSystemWrapper: ActorSystemWrapper,
                 streamingComponents: StreamingComponents
                )
  extends Health {
  implicit val system: ActorSystem = actorSystemWrapper.system
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = actorSystemWrapper.materializer
  val log: LoggingAdapter = system.log

  implicit val timeout: Timeout = Timeout(30 seconds)

  val routes: Route =
    path("api" / "classtimetables") {
      parameters('className.?, 'timeToTeachUserId.?) {
        (classNameOption, timeToTeachUserIdOption) =>
          get {
            val initialRequestReceived = Instant.now().toEpochMilli
            log.debug(s"Looking for Class Name ${classNameOption.getOrElse("NONE")} & " +
              s" tttId ${timeToTeachUserIdOption.getOrElse("NONE")}")
            failWith(new NotImplementedException())
          }
      }
    } ~ health


}
