package qizero.persistence.mapper

import language.experimental.macros
import qizero.model.Page
import scala.reflect.macros.whitebox.Context

trait Mapper[R, E] extends (R => E)

object Mapper {

  implicit final class OptionMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Option[R], Option[E]] {
    def apply(r: Option[R]): Option[E] = r.map(mapper)
  }

  implicit final class SeqMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Seq[R], Seq[E]] {
    def apply(r: Seq[R]): Seq[E] = r.map(mapper)
  }

  implicit final class PageMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Page[R], Page[E]] {
    def apply(r: Page[R]): Page[E] = Page(r.content.map(mapper), r.pagination, r.total)
  }

  implicit def create[R, E]: Mapper[R, E] = macro MapperMacro.create[R, E]

}

private object MapperMacro {
  def create[R: c.WeakTypeTag, E: c.WeakTypeTag](c: Context) = {
    import c.universe._
    val r = weakTypeOf[R]
    val e = weakTypeOf[E]
    println(showRaw(r))
    println(showRaw(e))
    val ctor = e.decl(termNames.CONSTRUCTOR).asMethod
    val paramss = ctor.paramLists.head.map { p =>
      val termName = p.name.toTermName
      println(showRaw(p))
      println(showRaw(p.name))
      println(showRaw(p.typeSignature))
      termName match {
        case TermName("id") => q"r.id.get"
        case n => q"r $n"
      }
    }
    println(showRaw(paramss))

    val result = q"""new Mapper[$r, $e]{ def apply(r:$r):$e = new $e(..$paramss) }"""
    println(showRaw(result))
    println(showCode(result))
    result
  }
}

