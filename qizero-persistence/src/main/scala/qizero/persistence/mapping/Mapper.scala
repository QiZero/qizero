package qizero.persistence.mapping

import language.experimental.macros

trait Mapper[R, E] extends (R => E)

object Mapper {
  def apply[R, E](mapper: R => E) = new Mapper[R, E] {
    def apply(r: R): E = mapper(r)
  }

//  def create[R, E]: Mapper[R, E] = macro MapperMacro.create[R, E]

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

