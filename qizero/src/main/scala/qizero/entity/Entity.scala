package qizero.entity

import org.joda.time.DateTime

trait Entity {
  type ID
  def id: ID
}

trait Id[I] {
  _: Entity =>
  type ID = I
  def id: ID
}

trait CreatedAt {
  _: Entity =>
  def createdAt: DateTime
}

trait UpdatedAt {
  _: Entity =>
  def updatedAt: DateTime
}

trait Timestamp extends CreatedAt with UpdatedAt {
  _: Entity =>
}