package qizero.persistence

import scala.slick.lifted.{AbstractTable, Column}
import org.joda.time.DateTime

trait HasUpdatedAt {
  _: AbstractTable[_] =>

  def updatedAt: Column[DateTime]
}

trait HasCreatedAt {
  _: AbstractTable[_] =>

  def createdAt: Column[DateTime]
}

trait HasId[ID] {
  _: AbstractTable[_] =>

  def id: Column[ID]
}

trait HasAutoIncId[ID] extends HasId[ID] {
  _: AbstractTable[_] =>
}

trait AutoIncId[ID] {
  def withId(id: ID): AutoIncId[ID]
}