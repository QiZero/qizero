package qizero.persistence.mapper

import java.sql.Timestamp
import org.joda.time.DateTime
import qizero.persistence.HasProfile

trait DateMapper {
  _: HasProfile =>

  import profile.simple._

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timestamp => new DateTime(timestamp.getTime)
  )

}
