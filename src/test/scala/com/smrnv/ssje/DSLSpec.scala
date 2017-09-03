package com.smrnv.ssje

import com.smrnv.ssje.DSL._
import org.scalatest.{Matchers, WordSpecLike}
import spray.json.DefaultJsonProtocol._
import spray.json._


class DSLSpec extends WordSpecLike with Matchers {

  "DSL" when {

    ">>" should {
      "perform field search by name and return JsValue" in {
        RawJson().parseJson >> "findMe" shouldBe JsString("field2-lvl1")
      }
      "throw FieldNotFound if no any field with the given element found" in {
        val ex = the[FieldNotFound] thrownBy RawJson().parseJson >> "FindMe"
        ex.getMessage should include("FindMe")
        ex.getMessage should include(s"${RawJson().parseJson.prettyPrint}")
      }
      "be able to perform nested search" in {
        RawJson().parseJson >> "findMeDeeper" shouldBe JsString("field2-lvl2-deeper")
        RawJson().parseJson >> "lvl2" shouldBe JsObject("findMe" -> JsString("field1-lvl2"))
        RawJson().parseJson >> "field3" >> "findMe" shouldBe JsString("field3-lvl1")
      }
    }

    ">?" should {
      "perform field search by name and return Option[JsValue]" in {
        RawJson().parseJson >? "findMe" shouldBe Some(JsString("field2-lvl1"))
      }
      "return None no any field with the given element found" in {
        RawJson().parseJson >? "FindMe" shouldBe None
      }
      "be able to perform nested search" in {
        RawJson().parseJson >? "findMeDeeper" shouldBe Some(JsString("field2-lvl2-deeper"))
        RawJson().parseJson >? "lvl2" shouldBe Some(JsObject("findMe" -> JsString("field1-lvl2")))
        RawJson().parseJson >? "field3" >? "findMe" shouldBe Some(JsString("field3-lvl1"))
      }
    }

    "convertTo[T]" should {
      "for optional JsValue should return Option[T]" in {
        (RawJson().parseJson >? "findMe").convertTo[String] shouldBe Some("field2-lvl1")
      }
      "for optional JsValue throws native error if deserialization impossible" in {
        the[spray.json.DeserializationException] thrownBy (RawJson().parseJson >? "findMe").convertTo[Int]
      }
    }

    "provide ability to combine >> and >? methods in any order" in {
      RawJson().parseJson >? "field2" >> "lvl2" >? "findMe" shouldBe Some(JsString("field2-lvl2"))
      RawJson().parseJson >? "field2" >? "lvl2" >> "findMe" shouldBe JsString("field2-lvl2")
      RawJson().parseJson >> "field2" >? "lvl2" >> "findMe" shouldBe JsString("field2-lvl2")
    }

  }
}
