package qizero.persistence.mapping

import qizero.persistence.HasProfile
import scala.util.Try

trait EnumMapper {
  _: HasProfile =>

  import profile.simple._

  protected final def stringEnumMapper(enum: Enumeration) = MappedColumnType.base[enum.Value, String](
    e => e.toString,
    s => Try(enum.withName(s)).getOrElse(throw new IllegalArgumentException(s"enumeration $s doesn't exist $enum [${enum.values.mkString(",")}]"))
  )

  protected final def intEnumMapper(enum: Enumeration) = MappedColumnType.base[enum.Value, Int](
    e => e.id,
    i => enum.apply(i)
  )
}
