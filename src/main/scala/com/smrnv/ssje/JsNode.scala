package com.smrnv.ssje

import spray.json.{JsArray, JsObject, JsValue}

import scala.annotation.tailrec


private[ssje] object JsNode {
  def apply(pair: (String, JsValue)): JsNode = pair match {
    case (name, node) =>
      new JsNode(Some(name), node)
  }

  def apply(node: JsValue): JsNode =
    new JsNode(None, node)

  @tailrec
  def search(name: String, nodes: Vector[JsNode]): Option[JsNode] =
    nodes.find(_.name.contains(name)) match {
      case result@Some(_) =>
        result
      case None if nodes.nonEmpty =>
        search(name, nodes.flatMap(_.children))
      case _ =>
        None
    }
}

/*
* Wrapper for the native JsValue, provides some extra methods
* */
private[ssje] case class JsNode(name: Option[String], value: JsValue) {

  def prettyPrint(takeFirst: Option[Int] = None): String = {
    val str = value.prettyPrint
    takeFirst match {
      case Some(len) if len < str.length =>
        s"${str.take(len)}\n<...>"
      case _ =>
        str
    }
  }

  def search(name: String): Option[JsNode] = {
    JsNode.search(name, this.children)
  }

  /*
  * first level of JsValue descendants
  * */
  final def children: Vector[JsNode] = value match {
    case JsArray(n) =>
      n.map(JsNode.apply)
    case JsObject(nodes) =>
      nodes.toVector.map(JsNode.apply)
    case _ =>
      Vector.empty
  }

}
