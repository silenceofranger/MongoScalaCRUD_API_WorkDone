package org.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.db.data.Client
import org.domain.ClientRequest
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat, RootJsonFormat}

trait JsonUtils extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val employeeJsonFormatter: RootJsonFormat[Client] = DefaultJsonProtocol.jsonFormat4(Client)
  implicit val employeeRequestFormat: RootJsonFormat[ClientRequest] = jsonFormat3(ClientRequest)

}
