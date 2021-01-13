package org.user.actor

import akka.actor.{Actor, ActorLogging}
import org.domain.ClientRequest
import org.service.ClientService

class ClientActor extends Actor with ActorLogging {

  private val clientService: ClientService = new ClientService()

  override def receive: Receive = {
    case SAVE(client: ClientRequest) =>
      log.info(s"CLIENT GETTING SAVED : $client")
      sender ! clientService.saveClientData(client)

    case SEARCH_ALL =>
      log.info(s"SHOW ALL")
      sender() ! clientService.findAll

    case UPDATE(cl,id) =>
      log.info(s"UPDATE DONE ON ID : $id")
      sender() ! clientService.update(cl,id)

    case DELETE(id)=>
      log.info(s"CLIENT WITH THIS ID IS DELETED: $id")
      sender() ! clientService.delete(id)

    case SEARCH_SOME(pageNumber, messagesPerPage) =>
      log.info(s"REQUEST RECEIVED PAGINATION DONE")
      sender() ! clientService.findSome(pageNumber, messagesPerPage)

    case _ =>
      log.debug("Request not supported")

  }
}

sealed trait ClientActorMessage

case class SAVE(cl: ClientRequest) extends ClientActorMessage

case object SEARCH_ALL extends ClientActorMessage

case class SEARCH_SOME(pageNumber: Int, messagesPerPage: Int) extends ClientActorMessage

case class UPDATE(cl: ClientRequest, id: String) extends ClientActorMessage

case class DELETE(id: String) extends ClientActorMessage