package org

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{get, path, post, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{PathDirectives, RouteDirectives}
import akka.pattern.Patterns
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import akka.util.ByteString
import org.db.data.Client
import org.domain.ClientRequest
import org.user.actor._
import org.utils.{JsonUtils, TimeUtils}
import spray.json.enrichAny

import scala.concurrent.Await

class ClientRouteConfig(implicit val system: ActorSystem) extends JsonUtils {
  val clientActor: ActorRef = system.actorOf(Props(new ClientActor()))

  implicit val mat: ActorMaterializer = ActorMaterializer()


  val getRoute: Route =

    PathDirectives.pathPrefix("client") {
      concat(
        path("create") {
          post {
            entity(as[ClientRequest]) { client =>
              val future = Patterns.ask(clientActor, SAVE(client), TimeUtils.timeoutMills)
              Await.result(future, TimeUtils.atMostDuration)
              RouteDirectives.complete(HttpEntity("Data saved successfully!"))
            }
          }
        },

        path("search") {
          get {
            val resultFuture = Patterns.ask(clientActor, SEARCH_ALL, TimeUtils.timeoutMills)
            val resultSource = Await.result(resultFuture, TimeUtils.atMostDuration).asInstanceOf[Source[Client, NotUsed]]
            val resultByteString = resultSource.map { it => ByteString.apply(it.toJson.toString.getBytes()) }
            RouteDirectives.complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, resultByteString))
          }
        },

        path("update") {
          put {
            parameter("id") { id =>
              entity(as[ClientRequest]) { client =>
                val future = Patterns.ask(clientActor, UPDATE(client, id), TimeUtils.timeoutMills)
                Await.result(future, TimeUtils.atMostDuration)
                RouteDirectives.complete(HttpEntity("Data updated saved successfully!"))
              }
            }
          }
        },

        path("delete") {
          delete {
            parameter("id") { id =>
              val resultFuture = Patterns.ask(clientActor, DELETE(id), TimeUtils.timeoutMills)
              Await.result(resultFuture, TimeUtils.atMostDuration)
              RouteDirectives.complete(HttpEntity(s"Data updated saved successfully!"))
            }
          }
        },

          path("pagination") {
          get {
            parameter("pageNumber","messagesPerPage") { (pageNumber, messagesPerPage) =>
              val resultFuture = Patterns.ask(clientActor, SEARCH_SOME(pageNumber.toInt, messagesPerPage.toInt), TimeUtils.timeoutMills)
              val resultSource = Await.result(resultFuture, TimeUtils.atMostDuration).asInstanceOf[Source[Client, NotUsed]]
              val resultByteString = resultSource.map { it => ByteString.apply(it.toJson.toString.getBytes()) }
              RouteDirectives.complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, resultByteString))
              //RouteDirectives.complete(HttpEntity(s"Data updated saved successfully!"))
            }
          }
        }
      )
    }
}

