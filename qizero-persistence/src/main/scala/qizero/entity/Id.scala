package qizero.entity

import scala.language.implicitConversions

trait TypedId extends Any {
  def value: Long
}

trait TypedIdFactory[I <: TypedId] {
  def apply(value: Long): I
}

trait TypedIdOrdering[I <: TypedId] {

  implicit object ordering extends Ordering[I] {
    def compare(a: I, b: I): Int = a.value compare b.value
  }

}

trait TypedIdCompanion[I <: TypedId] extends TypedIdFactory[I] with TypedIdOrdering[I] {
  implicit final def long2Id(value: Long): I = apply(value)
}
