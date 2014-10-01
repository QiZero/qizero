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
      idFormat.reads(json).map(id => HasId(id)).orElse(format.reads(json).map(e => HasEntity(e)))
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

sealed trait HasMany[+E <: Entity] {
  def ids: List[E#ID]
  def entities: List[E]
  def isIds: Boolean
  def isEntities: Boolean
}

object HasMany {
  val empty = HasEmpty

  def apply[E <: Entity](ids: List[E#ID]) = HasIds[E](ids)
  def apply[E <: Entity](entities: List[E]) = HasEntities[E](entities)

  implicit def fromEntity[E <: Entity](entities: List[E]) = apply(entities)
  implicit def fromId[E <: Entity](ids: List[E#ID]) = apply(ids)

  implicit def HasWrites[E <: Entity](implicit format: Writes[E], idFormat: Writes[E#ID]) = new Writes[HasMany[E]] {
    def writes(o: HasMany[E]): JsValue = {
      if(o.isEntities) Writes.traversableWrites[E].writes(o.entities)
      else Writes.traversableWrites[E#ID].writes(o.ids)
    }
  }

  implicit def HasReads[E <: Entity](implicit format: Reads[E], idFormat: Reads[E#ID]) = new Reads[HasMany[E]] {
    def reads(json: JsValue): JsResult[HasMany[E]] = json match{
      case JsArray(elems) if elems.isEmpty => JsSuccess(HasMany.empty)
      case _ => Reads.traversableReads[List, E#ID].reads(json).map(id => HasIds(id)).orElse {
        Reads.traversableReads[List, E].reads(json).map(e => HasEntities(e))
      }
    }
  }
}

final case class HasIds[E <: Entity](ids: List[E#ID]) extends HasMany[E] {
  def entities = throw new NoSuchElementException("Has.entity on HasId")
  val isIds = true
  val isEntities = false
}

final case class HasEntities[E <: Entity](entities: List[E]) extends HasMany[E] {
  lazy val ids = entities.map(_.id)
  val isIds = true
  val isEntities = true
}

object HasEmpty extends HasMany[Nothing] {
  val ids = Nil
  val entities = Nil
  val isEntities = true
  val isIds = true
}