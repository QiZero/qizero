package qizero.event

import org.joda.time.DateTime

trait Event {
  val createdAt: DateTime = DateTime.now
}


