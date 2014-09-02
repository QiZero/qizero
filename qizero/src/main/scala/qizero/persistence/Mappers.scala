package qizero.persistence

import java.sql.Timestamp
import scala.util.Try
import org.joda.time.DateTime

trait DateMapper {
  self: HasProfile =>

  import profile.simple._

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timestamp => new DateTime(timestamp.getTime)
  )

}

trait EnumMapper {
  self: HasProfile =>

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