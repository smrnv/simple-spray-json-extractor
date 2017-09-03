package com.smrnv.ssje

object RawJson {
  def apply(jsonInsideArray: String = ""): String =
    s"""
       |{
       |   "array": [$jsonInsideArray],
       |   "field1": {
       |     "lvl2" : {
       |       "findMe": "field1-lvl2"
       |     }
       |   },
       |   "field2": {
       |     "lvl2" : {
       |       "findMe": "field2-lvl2",
       |       "findMeDeeper": "field2-lvl2-deeper"
       |     },
       |     "findMe": "field2-lvl1"
       |   },
       |   "field3": {
       |     "findMe": "field3-lvl1"
       |   }
       |}
    """.stripMargin
}
