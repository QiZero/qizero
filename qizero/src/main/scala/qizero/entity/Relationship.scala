package qizero.entity

import java.util.NoSuchElementException
import scala.language.implicitConversions
import play.api.libs.json._

sealed trait Has[E <: Entity] {
  def id: E#ID
  def entity: E
  def isId: Boolean
  def isEntity: Boolean
}

object Has {
  def apply[E <: Entity](entity: E) = HasEntity[E](entity)
  def apply[E <: Entity](id: E#ID) = HasId[E](id)

  implicit def fromEntity[E <: Entity](entity: E) = apply(entity)
  implicit def fromId[E <: Entity](id: E#ID) = apply(id)

  implicit def HasWrites[E <: Entity](implicit format: Writes[E], idFormat: Writes[E#ID]) = new Writes[Has[E]] {
    def writes(o: Has[E]): JsValue = {
      if (o.isEntity) format.writes(o.entity)
      else idFormat.writes(o.id)
    }
  }

  implicit def HasReads[E <: Entity](implicit format: Reads[E], idFormat: Reads[E#ID]) = new Reads[Has[E]] {
    def reads(json: JsValue): JsResult[Has[E]] = {
      idFormat.reads(json).map(id => Has(id)).orElse(format.reads(json).map(e => Has(e)))
    }
  }
}

final case class HasId[E <: Entity](id: E#ID) extends Has[E] {
  def entity: E = throw new NoSuchElementException("Has.entity on HasId")
  val isId: Boolean = true
  val isEntity = false
}

final case class HasEntity[E <: Entity](entity: E) extends Has[E] {
  val id = entity.id
  val isId = true
  val isEntity = true
}

sealed trait HasMany[E <: Entity] {
  def ids: Seq[E#ID]
  def entities: Seq[E]
  def isIds: Boolean
  def isEntities: Boolean
}

object HasMany {

  def apply[E <: Entity](ids: Seq[E#ID]) = HasIds[E](ids)
  def apply[E <: Entity](entities: Seq[E]) = HasEntities[E](entities)

  def empty = HasNone
}

object HasNone extends HasMany[Nothing] {
  val ids = Seq.empty
  val entities = Seq.empty
  val isEntities = true
  val isIds = true
}

final case class HasIds[E <: Entity](ids: Seq[E#ID]) extends HasMany[E] {
  def entities = throw new NoSuchElementException("Has.entity on HasId")
  val isIds = true
  val isEntities = false
}

final case class HasEntities[E <: Entity](entities: Seq[E]) extends HasMany[E] {
  lazy val ids = entities.map(_.id)
  val isIds = true
  val isEntities = true
}