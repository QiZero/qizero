package qizero.model.format

import play.api.libs.json._

trait EnumerationFormat {
  protected final def enumReads[E <: Enumeration](enum: E): Reads[E#Value] =
    new Reads[E#Value] {
      def reads(json: JsValue): JsResult[E#Value] = json match {
        case JsString(s) => {
          try {
            JsSuccess(enum.withName(s))
          } catch {
            case _: NoSuchElementException =>
              JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
          }
        }
        case JsNumber(id) => {
          try {
            JsSuccess(enum(id.intValue()))
          } catch {
            case _: NoSuchElementException =>
              JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$id'")
          }
        }
        case _ => JsError("String value expected")
      }
    }

  protected final def enumWrites[E <: Enumeration](useId: Boolean): Writes[E#Value] =
    new Writes[E#Value] {
      def writes(v: E#Value): JsValue = if (useId) JsNumber(v.id) else JsString(v.toString)
    }

  protected final def enumFormat[E <: Enumeration](enum: E, useId: Boolean): Format[E#Value] = {
    Format(enumReads(enum), enumWrites(useId))
  }
}
