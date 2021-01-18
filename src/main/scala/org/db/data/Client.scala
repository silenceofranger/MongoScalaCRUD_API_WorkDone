package org.db.data

import java.lang.annotation.Documented

@Documented
case class Client(_id: String, name: String, inboundFeedURL: String)
