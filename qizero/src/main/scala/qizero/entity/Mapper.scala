package qizero.entity

import scala.annotation.implicitNotFound
import scala.collection.Traversable
import scala.language.implicitConversions
import qizero.model._

@implicitNotFound("No implicit Mapper defined for ${IN} -> ${OUT}.")
trait Mapper[IN, OUT] extends (IN => OUT)

object Mapper extends DefaultMapper {
  def apply[IN, OUT](f: IN => OUT) = new Mapper[IN, OUT] {
    def apply(in: IN): OUT = f(in)
  }
}

trait DefaultMapper {
  implicit def toOption[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Option[IN], Option[OUT]] = {
    Mapper { in: Option[IN] => in.map(mapper)}
  }

  implicit def toTranversable[IN, OUT](mapper: Mapper[IN, OUT]): Mapper[Traversable[IN], Traversable[OUT]] = {
    Mapper { in: Traversable[IN] => in.map(mapper)}
  }

  implicit def toList[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: List[IN] => in.map(mapper)}
  }

  implicit def toPage[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: Page[IN] => in.map(mapper)}
  }

  implicit def toSlice[IN, OUT](mapper: Mapper[IN, OUT]) = {
    Mapper { in: Slice[IN] => in.map(mapper)}
  }
}
