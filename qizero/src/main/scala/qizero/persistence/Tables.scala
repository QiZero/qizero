package qizero.persistence

import org.joda.time.DateTime
import scala.slick.lifted.{AbstractTable, Rep}

trait HasUpdatedAt {
  _: AbstractTable[_] =>

  def updatedAt: Rep[DateTime]
}

trait HasCreatedAt {
  _: AbstractTable[_] =>

  def createdAt: Rep[DateTime]
}

trait HasId[ID] {
  _: AbstractTable[_] =>

  def id: Rep[ID]
}

trait HasAutoIncId[ID] extends HasId[ID] {
  _: AbstractTable[_] =>
}

trait AutoIncId[ID] {
  def withId(id: ID): AutoIncId[ID]
}