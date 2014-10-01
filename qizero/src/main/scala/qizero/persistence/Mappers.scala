package qizero.persistence

import java.sql._
import scala.slick.jdbc.{GetResult, SetParameter}
import scala.util.Try
import org.joda.time._

trait DateMapper {
  self: HasProfile =>

  import profile.simple._

  implicit val GetDateTime = GetResult[DateTime](r => new DateTime(r.nextTimestamp.getTime))
  implicit val SetDateTime = SetParameter[DateTime]((t, p) => p.setTimestamp(new Timestamp(t.getMillis)))

  implicit val DateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timestamp => new DateTime(timestamp.getTime)
  )

  implicit val GetLocalDate = GetResult[LocalDate](r => new LocalDate(r.nextDate.getTime))
  implicit val SetLocalDate = SetParameter[LocalDate]((t, p) => p.setDate(new Date(t.toDate.getTime)))

  implicit val LocalDateMapper = MappedColumnType.base[LocalDate, Date](
    dt => new Date(dt.toDate.getTime),
    d => new LocalDate(d.getTime)
  )

}

trait EnumerationMapper {
  self: HasProfile =>

  import profile.simple._

  protected final def enumValueMapper(enum: Enumeration) = MappedColumnType.base[enum.Value, String](
    e => e.toString,
    s => Try(enum.withName(s)).getOrElse(throw new IllegalArgumentException(s"enumeration $s doesn't exist $enum [${enum.values.mkString(",")}]"))
  )

  protected final def enumIdMapper(enum: Enumeration) = MappedColumnType.base[enum.Value, Int](
    e => e.id,
    i => enum.apply(i)
  )
}