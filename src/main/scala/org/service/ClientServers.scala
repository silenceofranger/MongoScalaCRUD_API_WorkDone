package org.service

import java.util.UUID

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.db.data.Client
import org.domain.ClientRequest
import org.mongodb.scala.Completed
import org.mongodb.scala.result.DeleteResult
import org.user.dbclientrepo.DBClientRepo

import scala.concurrent.{ExecutionContextExecutor, Future}

object ClientServers {

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val mat: ActorMaterializer = ActorMaterializer()


  def saveClientData: ClientRequest => Future[Completed] = (clientRequest: ClientRequest) => {
    val clientDoc: Client = clientMapperWithNewID(clientRequest)

    DBClientRepo.insertData(clientDoc)
  }

  def findAll: Source[Client, NotUsed] = {
    Source.fromFuture(DBClientRepo.findAll())
      .mapConcat {
        identity
      }
  }


  def findSome(pageNumber: Int, messagesPerPage: Int) = {
    Source.fromFuture(DBClientRepo.findSome(pageNumber, messagesPerPage))
      .mapConcat {
        identity
      }
  }

  def update(clientRequest: ClientRequest, id: String): Future[Client] = {
    val clientDoc: Client = clientMapperWithNewID(clientRequest)
    DBClientRepo.update(cl = clientDoc, id)
  }

  def delete(id: String): Future[DeleteResult] = {
    DBClientRepo.delete(id)
  }

  private def clientMapperWithNewID(client: ClientRequest) = {
    Client(name = client.name, inboundFeedURL = client.inboundFeedURL, _id = UUID.randomUUID.toString)
  }

}
