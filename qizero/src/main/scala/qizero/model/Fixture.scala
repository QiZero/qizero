package qizero.model

import scala.annotation.implicitNotFound
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import org.joda.time._

trait Fixture extends DefaultFixtures {
  def seed[T](implicit factory: Fixture.Factory[T]): T = factory()
}

object Fixture extends Fixture {

  @implicitNotFound("No implicit Fixture Factory defined for ${T}.")
  trait Factory[T] extends (() => T)

  object Factory {
    def apply[T](f: () => T) = new Fixture.Factory[T] {
      def apply() = f()
    }

    implicit def factory[T]: Factory[T] = macro materialize[T]

    def materialize[T: c.WeakTypeTag](c: Context) = {
      import c.universe._
      val tpe = weakTypeOf[T]

      val random = q"scala.util.Random"
      val fixture = q"_root_.qizero.model.Fixture"

      val constructor = tpe.decl(termNames.CONSTRUCTOR) match {
        case NoSymbol => c.abort(c.enclosingPosition, "model constructor not found")
        case s => s.asMethod
      }

      val params = constructor.paramLists.head

      val seedParams = params.flatMap { param =>
        val paramName = param.name.toTermName
        val paramType = param.typeSignature

        if (param.asTerm.isParamWithDefault) None
        else if (paramType <:< typeOf[String]) Some( q"""$paramName = ${paramName.decodedName.toString + "-"}+$random.nextInt(10000)""")
        else if (paramType <:< typeOf[Boolean]) Some( q"""$paramName = $random.nextBoolean""")
        else if (paramType <:< typeOf[Byte]) Some( q"""$paramName = $random.nextInt.toByte""")
        else if (paramType <:< typeOf[Short]) Some( q"""$paramName = $random.nextInt.toShort""")
        else if (paramType <:< typeOf[Int]) Some( q"""$paramName = $random.nextInt""")
        else if (paramType <:< typeOf[Long]) Some( q"""$paramName = $random.nextLong""")
        else if (paramType <:< typeOf[Float]) Some( q"""$paramName = $random.nextFloat""")
        else if (paramType <:< typeOf[Double]) Some( q"""$paramName = $random.nextDouble""")
        else if (paramType <:< typeOf[BigDecimal]) Some( q"""$paramName = BigDecimal($random.nextInt)""")
        else if (paramType <:< typeOf[Option[_]]) Some(q"$paramName = None")
        else Some(q"$paramName = $fixture.seed[$paramType]")
      }

      q"""new $fixture.Factory[$tpe]{ def apply():$tpe = new $tpe(..$seedParams) }"""
    }
  }

}

trait DefaultFixtures {
  implicit val DateTimeFactory = Fixture.Factory(() => DateTime.now())
  implicit val LocalDateFixture = Fixture.Factory(() => LocalDate.now())
}
