package qizero.entity

import java.util.NoSuchElementException

sealed abstract class Has[ID, E <: Entity[ID]] {
  def id: ID
  def entity: E
  def hasId: Boolean
  def hasEntity: Boolean
}

final case class HasId[ID, E <: Entity[ID]](id: ID) extends Has[ID, E] {
  def entity: E = throw new NoSuchElementException("Has.entity on HasId")
  val hasId: Boolean = true
  val hasEntity = false
}

final case class HasEntity[ID, E <: Entity[ID]](entity: E) extends Has[ID, E] {
  val id: ID = entity.id
  val hasId: Boolean = true
  val hasEntity: Boolean = true
}


sealed abstract class HasMany[ID, E <: Entity[ID]] {
  def ids: Seq[ID]
  def entities: Seq[E]
  def hasIds: Boolean
  def hasEntities: Boolean
}

final case class HasManyId[ID, E <: Entity[ID]](ids: Seq[ID]) extends HasMany[ID, E] {
  def entities: Seq[E] = throw new NoSuchElementException("Has.entity on HasId")
  val hasIds: Boolean = true
  val hasEntities: Boolean = false
}

final case class HasManyEntity[ID, E <: Entity[ID]](entities: Seq[E]) extends HasMany[ID, E] {
  val ids: Seq[ID] = entities.map(_.id)
  val hasIds: Boolean = true
  val hasEntities: Boolean = true
}