package qizero.persistence.mapping

import language.experimental.macros
import scala.reflect.macros.whitebox.Context

trait Mapper[R, E] extends (R => E)

object Mapper {
  def apply[R, E](mapper: R => E) = new Mapper[R, E] {
    def apply(r: R): E = mapper(r)
  }

  def create[R, E]: Mapper[R, E] = macro MapperBuilder.create[R, E]

}

private object MapperBuilder {
  def create[IN: c.WeakTypeTag, OUT: c.WeakTypeTag](c: Context) = {
    import c.universe._

    val inType = weakTypeOf[IN]
    val outType = weakTypeOf[OUT]
    println("row: " + showRaw(inType))
    println("entity:" + showRaw(outType))

    val a = inType.decls
    val b = inType.members

    val inConstructor = inType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "No in Constrcutor found")
      case s => s.asMethod
    }
    val inParams = inConstructor.paramLists.head

    val outConstructor = outType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "No out constructur found")
      case s => s.asMethod
    }
    val outParams = outConstructor.paramLists.head

    val matchParams = outParams.forall(op => inParams.exists(ip => op.name == ip.name))

    def mapParams() = outParams.map { param =>
      val paramName = param.name.toTermName
      val paramType = param.typeSignature
      val inParam = inParams.find(_.name == paramName).get
      val inParamName = inParam.name.toTermName
      val inParamType = inParam.typeSignature
      println(showRaw(paramName) + " - " + showRaw(inParamName))
      println(showRaw(paramType) + " - " + showRaw(inParamType))
      if (paramType =:= inParamType) q"in.$paramName"
      else if (inParamType <:< typeOf[Option[_]]) q"in.$paramName.get"
      else c.abort(c.enclosingPosition, "Missing param " + paramName)
    }

    def mapTupleParams() = {
      outParams
      c.abort(c.enclosingPosition, "Invalid param number and type")
    }

    val params = {
      if (matchParams) mapParams()
      else mapTupleParams()
    }

    println(showRaw(params))

    val result = q"""new Mapper[$inType, $outType]{def apply(in:$inType):$outType = new $outType(..$params) }"""
    println(showRaw(result))
    println(showCode(result))
    result
  }
}

