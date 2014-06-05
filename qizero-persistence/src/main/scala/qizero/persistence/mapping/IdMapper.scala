package qizero.persistence.mapping

import qizero.entity.{TypedIdFactory, TypedId}
import qizero.persistence.HasProfile
import scala.reflect.ClassTag

trait TypedIdMapper {
  _: HasProfile =>

  import profile.simple._

  protected final def idMapper[T <: TypedId : ClassTag](f: TypedIdFactory[T]) = MappedColumnType.base[T, Long](
    id => id.value,
    n => f(n)
  )
}
