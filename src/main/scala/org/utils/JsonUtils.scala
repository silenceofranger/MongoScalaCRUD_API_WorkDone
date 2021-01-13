package org.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.db.data.Client
import org.domain.ClientRequest
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}

trait JsonUtils extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val employeeJsonFormatter: RootJsonFormat[Client] = DefaultJsonProtocol.jsonFormat3(Client)
  implicit val employeeRequestFormat: RootJsonFormat[ClientRequest] = jsonFormat2(ClientRequest)

}
