package qizero.persistence.table

import scala.slick.lifted.{Column, AbstractTable}

trait HasId[ID] {
  _: AbstractTable[_] =>

  def id: Column[ID]
}