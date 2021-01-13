package org.user.actor

import akka.actor.{Actor, ActorLogging}
import org.domain.ClientRequest
import org.service.ClientService

class ClientActor extends Actor with ActorLogging {

  private val clientService: ClientService = new ClientService()

  override def receive: Receive = {
    case SAVE(client: ClientRequest) =>
      log.info(s"received message Save with client $client")
      sender ! clientService.saveClientData(client)

    case SEARCH_ALL =>
      log.info(s"received message find all")
      sender() ! clientService.findAll

    case UPDATE(cl,id) =>
      log.info(s"received message find all")
      sender() ! clientService.update(cl,id)

    case DELETE(id)=>
      log.info(s"delete message received for the id: $id")
      sender() ! clientService.delete(id)

    case SEARCH_SOME(pageNumber, messagesPerPage) =>
      log.info(s"received message find some for pagination")
      sender() ! clientService.findSome(pageNumber, messagesPerPage)

    case _ =>
      log.debug("Unhandled message!")

  }
}

sealed trait ClientActorMessage

case class SAVE(cl: ClientRequest) extends ClientActorMessage

case object SEARCH_ALL extends ClientActorMessage

case class SEARCH_SOME(pageNumber: Int, messagesPerPage: Int) extends ClientActorMessage

case class UPDATE(cl: ClientRequest, id: String) extends ClientActorMessage

case class DELETE(id: String) extends ClientActorMessage



