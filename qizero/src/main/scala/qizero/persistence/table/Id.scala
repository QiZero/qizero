package qizero.persistence.table

import scala.slick.lifted.{Column, AbstractTable}

trait RowId[ID] {
  def withId(id: ID): RowId[ID]
}

trait HasId[ID] {
  _: AbstractTable[_] =>

  def id: Column[ID]
}