package org.user.repositories


import org.db.config.DbConfig
import org.db.data.Client
import org.mongodb.scala.{Completed, MongoCollection}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.result.DeleteResult
import org.utils.JsonUtils

import scala.concurrent.Future



object ClientRepo extends JsonUtils {

  private val clientDoc: MongoCollection[Client] = DbConfig.clients

  def createCollection(): Unit = {
    DbConfig.database.createCollection("client_collection").subscribe(
      (result: Completed) => println(s"$result"),
      (e: Throwable) => println(e.getLocalizedMessage),
      () => println("complete"))
  }

  def insertData(cl: Client): Future[Completed] = {
    clientDoc.insertOne(cl).toFuture()
  }

  def findAll(): Future[Seq[Client]] = {
    clientDoc.find().toFuture()
  }

  def findSome(pageNumber: Int, messagesPerPage: Int): Future[Seq[Client]] = {
    clientDoc.find().skip((pageNumber - 1) * messagesPerPage).limit(messagesPerPage).sort(ascending("name")).toFuture()
  }

  def update(cl: Client, id: String): Future[Client] = {
    clientDoc.findOneAndUpdate(equal("_id", id), setBsonValue(cl), FindOneAndUpdateOptions().upsert(true)).toFuture()
  }

  def delete(id: String): Future[DeleteResult] = {
    clientDoc.deleteOne(equal("id", id)).toFuture()
  }

  private def setBsonValue(client: Client): Bson = {
    combine(
      set("name", client.name),
      set("inboundFeedURL", client.inboundFeedURL),
      set("YOE", client.YOE)
    )
  }
}
