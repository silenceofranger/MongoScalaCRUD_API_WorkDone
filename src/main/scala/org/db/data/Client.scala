package org.db.data

import java.lang.annotation.Documented
//import java.time.LocalDate


@Documented
case class Client(_id: String, name: String, inboundFeedURL: String , YOE: Int)
