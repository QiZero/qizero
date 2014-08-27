package qizero.persistence.table

import org.joda.time.DateTime
import scala.slick.lifted.{Column, AbstractTable}

trait HasUpdatedAt {
  _: AbstractTable[_] =>

  def updatedAt: Column[DateTime]
}

trait HasCreatedAt {
  _: AbstractTable[_] =>

  def createdAt: Column[DateTime]
}
