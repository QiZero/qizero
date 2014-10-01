package qizero.model.format

import play.api.libs.json._

trait EnumerationFormat {

  protected final def enumValueReads[E <: Enumeration](enum: E): Reads[E#Value] = Reads {
    case JsString(s) =>
      try {
        JsSuccess(enum.withName(s))
      } catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
      }
    case _ => JsError("String value expected")
  }

  protected final def enumIdReads[E <: Enumeration](enum: E): Reads[E#Value] = Reads {
    case JsNumber(id) =>
      try {
        JsSuccess(enum(id.intValue()))
      } catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$id'")
      }
    case _ => JsError("String value expected")
  }

  protected final def enumValueWrites[E <: Enumeration](): Writes[E#Value] = {
    Writes { v: E#Value => JsString(v.toString)}
  }

  protected final def enumIdWrites[E <: Enumeration](): Writes[E#Value] = {
    Writes { v: E#Value => JsNumber(v.id)}
  }

  protected final def enumValueFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumValueReads(enum), enumValueWrites())
  }

  protected final def enumIdFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumIdReads(enum), enumIdWrites())
  }
}
