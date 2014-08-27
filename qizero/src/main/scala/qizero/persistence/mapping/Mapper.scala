package qizero.persistence.mapping

import scala.collection.Traversable
import scala.language.experimental.macros
import scala.language.implicitConversions
import qizero.model._

trait Mapper[IN, OUT] extends (IN => OUT)

object Mapper extends DefaultMapper {
  def apply[IN, OUT](f: IN => OUT) = new Mapper[IN, OUT] {
    def apply(in: IN): OUT = f(in)
  }
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

//private object MapperMacro {
//  def create[R: c.WeakTypeTag, E: c.WeakTypeTag](c: Context) = {
//    import c.universe._
//    val r = weakTypeOf[R]
//    val e = weakTypeOf[E]
//    println(showRaw(r))
//    println(showRaw(e))
//    val ctor = e.decl(termNames.CONSTRUCTOR).asMethod
//    val paramss = ctor.paramLists.head.map { p =>
//      val termName = p.name.toTermName
//      println(showRaw(p))
//      println(showRaw(p.name))
//      println(showRaw(p.typeSignature))
//      termName match {
//        case TermName("id") => q"r.id.get"
//        case n => q"r $n"
//      }
//    }
//    println(showRaw(paramss))
//
//    val result = q"""new Mapper[$r, $e]{ def apply(r:$r):$e = new $e(..$paramss) }"""
//    println(showRaw(result))
//    println(showCode(result))
//    result
//  }
//}

