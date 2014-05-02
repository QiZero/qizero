package qizero.entity

import org.joda.time.DateTime

trait Entity[ID] {
  def id: ID
}

trait CreatedAt {
  _: Entity[_] =>
  def createdAt: DateTime
}

trait UpdatedAt {
  _: Entity[_] =>
  def updatedAt: DateTime
}

trait Timestamp extends CreatedAt with UpdatedAt {
  _: Entity[_] =>
}