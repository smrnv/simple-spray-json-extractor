package com.smrnv.ssje

import spray.json.{JsValue, JsonReader}


object DSL extends DSL

trait DSL {

  protected val trimPrintedJson: Option[Int] = Some(1000)

  sealed trait DSLSyntax {
    protected def jsNode: Option[JsNode]

    def >?(name: String): Option[JsValue] =
      jsNode.flatMap(_.search(name)).map(_.value)

    def >>(name: String): JsValue =
      >?(name).getOrElse(throw FieldNotFound(name, jsNode.map(_.prettyPrint(trimPrintedJson)).getOrElse(None.toString)))
  }


  implicit class OptJsValueLifter(json: Option[JsValue]) extends DSLSyntax {
    override protected val jsNode: Option[JsNode] = json.map(JsNode.apply)

    def convertTo[T](implicit reader: JsonReader[T]): Option[T] =
      json.map(_.convertTo[T])
  }

  implicit class JsValueLifter(json: JsValue) extends DSLSyntax {
    override protected val jsNode: Option[JsNode] = Some(JsNode(json))
  }

}
