package com.smrnv.ssje

import scala.util.control.NoStackTrace

case class FieldNotFound(fieldName: String, json: String) extends NoStackTrace {
  override def getMessage: String =
    s"""Can't find field "$fieldName" in \n$json\n"""
}
