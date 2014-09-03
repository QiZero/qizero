package qizero.entity

import scala.annotation.implicitNotFound
import scala.collection.Traversable
import scala.language.experimental.macros
import scala.language.implicitConversions
import scala.reflect.macros.whitebox.Context
import qizero.model._

@implicitNotFound("No implicit Mapper defined for ${IN} -> ${OUT}.")
trait Mapper[IN, OUT] extends (IN => OUT)

object Mapper extends DefaultMapper {
  def apply[IN, OUT](f: IN => OUT) = new Mapper[IN, OUT] {
    def apply(in: IN): OUT = f(in)
  }

  def as[IN, OUT]:Mapper[IN,OUT] = macro MapperMacro.materialize[IN,OUT]
}

trait DefaultMapper {
  implicit def OptionMapper[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Option[IN], Option[OUT]] = {
    Mapper { in: Option[IN] => in.map(mapper)}
  }

  implicit def TranversableMapper[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Traversable[IN], Traversable[OUT]] = {
    Mapper { in: Traversable[IN] => in.map(mapper)}
  }

  implicit def ListMapper[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: List[IN] => in.map(mapper)}
  }

  implicit def PageMapper[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: Page[IN] => in.map(mapper)}
  }

  implicit def SliceMapper[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: Slice[IN] => in.map(mapper)}
  }
}

private object MapperMacro {
  def materialize[IN: c.WeakTypeTag, OUT: c.WeakTypeTag](c: Context) = {
    import c.universe._

    val inType = weakTypeOf[IN]
    val outType = weakTypeOf[OUT]
    println("row: " + showRaw(inType))
    println("entity:" + showRaw(outType))

    val inConstructor = inType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "No in constructor found")
      case s => s.asMethod
    }
    val inParams = inConstructor.paramLists.head

    val outConstructor = outType.decl(termNames.CONSTRUCTOR) match {
      case NoSymbol => c.abort(c.enclosingPosition, "No out constructor found")
      case s => s.asMethod
    }
    val outParams = outConstructor.paramLists.head

    def matchParams() = outParams.forall(op => inParams.exists(ip => op.name == ip.name))

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
      c.abort(c.enclosingPosition, "Invalid param number and type")
    }

    val params = {
      if (matchParams()) mapParams()
      else mapTupleParams()
    }

    println(showRaw(params))

    val result = q"""new Mapper[$inType, $outType]{def apply(in:$inType):$outType = new $outType(..$params) }"""
    println(showRaw(result))
    println(showCode(result))
    result
  }
}

