package qizero.entity

import qizero.model._
import scala.annotation.implicitNotFound
import scala.collection.Traversable
import scala.language.implicitConversions

@implicitNotFound("No implicit Mapper defined for ${IN} -> ${OUT}.")
trait Mapper[IN, OUT] extends (IN => OUT)

object Mapper extends DefaultMappers {
  def apply[IN, OUT](f: IN => OUT) = new Mapper[IN, OUT] {
    def apply(in: IN): OUT = f(in)
  }
}

trait DefaultMappers {
  implicit def toOption[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Option[IN], Option[OUT]] = {
    Mapper { in: Option[IN] => in.map(mapper)}
  }

  implicit def toTranversable[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Traversable[IN], Traversable[OUT]] = {
    Mapper { in: Traversable[IN] => in.map(mapper)}
  }

  implicit def toList[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[List[IN], List[OUT]] = {
    Mapper { in: List[IN] => in.map(mapper)}
  }

  implicit def toPage[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Page[IN], Page[OUT]] = {
    Mapper { in: Page[IN] => in.map(mapper)}
  }

  implicit def toSlice[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Slice[IN], Slice[OUT]] = {
    Mapper { in: Slice[IN] => in.map(mapper)}
  }
}
