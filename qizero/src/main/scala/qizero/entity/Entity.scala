package qizero.entity

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import org.joda.time.DateTime

trait Entity {
  type ID
  def id: ID
}

object Entity {
  def mapper[IN, OUT](f: IN => OUT) = Mapper(f)
  def mapper[IN, OUT]: Mapper[IN, OUT] = macro EntityMacros.materialize[IN, OUT]
}

trait Id[I] {
  _: Entity =>
  type ID = I
  def id: ID
}

trait CreatedAt {
  _: Entity =>
  def createdAt: DateTime
}

trait UpdatedAt {
  _: Entity =>
  def updatedAt: DateTime
}

trait Timestamp extends CreatedAt with UpdatedAt {
  _: Entity =>
}

private object EntityMacros {
  def materialize[IN: c.WeakTypeTag, OUT: c.WeakTypeTag](c: Context) = {
    import c.universe._

    val inType = weakTypeOf[IN]
    val outType = weakTypeOf[OUT]

    val inConstructor = inType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "IN constructor not found")
      case s => s.asMethod
    }
    val inParams = inConstructor.paramLists.head

    val outConstructor = outType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "OUT constructor not found")
      case s => s.asMethod
    }
    val outParams = outConstructor.paramLists.head

    def getParams() = outParams.flatMap { param =>
      val paramName = param.name.toTermName
      val paramType = param.typeSignature
      if (paramType <:< typeOf[Has[_]]) {
        val entityType = paramType.typeArgs(0)
        val inIdParamOpt = inParams.find(_.name.decodedName.toString == paramName.decodedName.toString + "Id")
        inIdParamOpt match {
          case Some(idParam) =>
            val idParamName = idParam.name.toTermName
            Some(q"$paramName = new _root_.qizero.entity.HasId[$entityType](in.$idParamName)")
          case None => c.abort(c.enclosingPosition, "Missing Param Id:" + paramName)
        }
      } else {
        val inParamOpt = inParams.find(_.name == paramName)
        inParamOpt match {
          case Some(inParam) =>
            val inParamName = inParam.name.toTermName
            val inParamType = inParam.typeSignature
            if (paramType =:= inParamType) Some(q"$paramName = in.$inParamName")
            else if (inParamType <:< typeOf[Option[_]] && inParamType.typeArgs(0) =:= paramType) Some(q"$paramName = in.$inParamName.get")
            else c.abort(c.enclosingPosition, "Param Type doesn't matched: " + paramName)
          case None if param.asTerm.isParamWithDefault => None
          case None => c.abort(c.enclosingPosition, "Missing Param: " + paramName)
        }
      }
    }

    val params = getParams()
    val result = q"""new _root_.qizero.entity.Mapper[$inType, $outType]{ def apply(in:$inType):$outType = new $outType(..$params) }"""
    result
  }
}