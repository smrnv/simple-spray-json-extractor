package com.smrnv.ssje

import org.scalatest.{Matchers, WordSpecLike}
import spray.json._
import spray.json.DefaultJsonProtocol._


class JsNodeSpec extends WordSpecLike with Matchers {

  "JsNode" when {

    ".children" should {
      "if JsNode wraps JsObject - return Vector of it's fields" in {
        val json = """{"field1": "value1", "field2": "value2"}""".parseJson
        val children = Vector(JsNode("field1" -> JsString("value1")), JsNode("field2" -> JsString("value2")))

        JsNode(json).children shouldBe children
        children shouldBe json.asJsObject.fields.toVector.map(JsNode.apply)
      }
      "if JsNode wraps JsArray - return Vector of all JsValues from array" in {
        val json = """[{"field1": "value1"}, "value2"]""".parseJson
        val children = Vector(JsNode(JsObject("field1" -> JsString("value1"))), JsNode(JsString("value2")))

        JsNode(json).children shouldBe children
        children shouldBe json.convertTo[Vector[JsValue]].map(JsNode.apply)
      }
      "return empty Vector in other cases" in {
        val json = """"value1"""".parseJson

        JsNode(json).children shouldBe Vector.empty
      }
    }

    ".search" should {
      "return first occurrence of field with the given name from the nearest level" in {
        JsNode(RawJson().parseJson).search("findMe") shouldBe Some(JsNode("findMe" -> JsString("field2-lvl1")))
      }
      "be able to perform top-level search" in {
        JsNode(RawJson().parseJson).search("field3") shouldBe Some(JsNode("field3" -> JsObject("findMe" -> JsString("field3-lvl1"))))
        JsNode(RawJson().parseJson).search("array") shouldBe Some(JsNode("array" -> JsArray.empty))
      }
      "be able to perform nested search" in {
        JsNode(RawJson().parseJson).search("findMeDeeper") shouldBe Some(JsNode("findMeDeeper" -> JsString("field2-lvl2-deeper")))
      }
      "correctly search if JsNode contains array" in {
        val rawJsonInsideArray = """{"findMeDeeper": "array0"}, {"findMeDeeper": "array1"}"""
        val json = RawJson(rawJsonInsideArray).parseJson

        JsNode(json).search("findMeDeeper") shouldBe Some(JsNode("findMeDeeper" -> JsString("array0")))
      }
      "return None if no any JsNode found" in {
        JsNode(RawJson().parseJson).search("FindMe") shouldBe None
      }
    }
  }

}
