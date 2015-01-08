package qizero.entity


import qizero.entity.TypedId.Factory
import scala.annotation.implicitNotFound
import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.whitebox
import scala.slick.lifted.MappedTo

trait TypedId extends Any with MappedTo[Long] {
  def value: Long

  override def toString: String = value.toString
}

object TypedId {

  @implicitNotFound("No implicit Factory defined for ${I}.")
  trait Factory[I <: TypedId] extends (Long => I)

  object Factory {
    implicit def materialize[T <: TypedId]: Factory[T] = macro TypedIdMacro.materializeFactory[T]
  }

  implicit def toId[T <: TypedId](value: Short)(implicit factory: Factory[T]): T = factory(value)

  implicit def toId[T <: TypedId](value: Int)(implicit factory: Factory[T]): T = factory(value)

  implicit def toId[T <: TypedId](value: Long)(implicit factory: Factory[T]): T = factory(value)

  implicit def toLong(id: TypedId): Long = id.value
}

private object TypedIdMacro {
  def materializeFactory[T <: TypedId : c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._

    val allImplicitCandidates = c.openImplicits.map(_.pt.dealias)
    val implicitCandidates = allImplicitCandidates.collect {
      case TypeRef(_, _, toType :: Nil) => toType
      case TypeRef(_, _, _ :: toType :: Nil) => toType
    }.filter(x => x.typeSymbol.isClass && x.typeSymbol.asClass.isCaseClass)

    implicitCandidates match {
      case to :: Nil =>
        q"""
          new _root_.qizero.entity.TypedId.Factory[$to] {
          def apply(id: Long) = new $to(id)
        }"""
      case l => c.abort(NoPosition, s"There are ${l.size} implicit candidates found. There should be only 1.")
    }
  }
}