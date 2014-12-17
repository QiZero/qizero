package qizero.entity

import java.util.NoSuchElementException
import play.api.libs.json._
import scala.language.implicitConversions

sealed trait Has[E <: EntityWithId] {
  def id: E#ID

  def entity: E

  def isId: Boolean

  def isEntity: Boolean
}

object Has {
  def apply[E <: EntityWithId](entity: E) = HasEntity[E](entity)

  def apply[E <: EntityWithId](id: E#ID) = HasId[E](id)

  implicit def fromEntity[E <: EntityWithId](entity: E): HasEntity[E] = apply(entity)

  implicit def fromId[E <: EntityWithId](id: E#ID): HasId[E] = apply(id)

  implicit def fromOptionEntity[E <: EntityWithId](entity: Option[E]): Option[HasEntity[E]] = entity.map(fromEntity)

  implicit def toOption[E <: EntityWithId](entity: Has[E]): Some[Has[E]] = Some(entity)

  implicit def HasWrites[E <: EntityWithId](implicit format: Writes[E], idFormat: Writes[E#ID]): Writes[Has[E]] = new Writes[Has[E]] {
    def writes(o: Has[E]): JsValue = {
      if (o.isEntity) format.writes(o.entity)
      else idFormat.writes(o.id)
    }
  }

  implicit def HasReads[E <: EntityWithId](implicit format: Reads[E], idFormat: Reads[E#ID]): Reads[Has[E]] = new Reads[Has[E]] {
    def reads(json: JsValue): JsResult[Has[E]] = {
      idFormat.reads(json).map(id => HasId(id)).orElse(format.reads(json).map(e => HasEntity(e)))
    }
  }
}

final case class HasId[E <: EntityWithId](id: E#ID) extends Has[E] {
  def entity: E = throw new NoSuchElementException("Has.entity on HasId")

  val isId = true
  val isEntity = false
}

final case class HasEntity[E <: EntityWithId](entity: E) extends Has[E] {
  val id = entity.id
  val isId = true
  val isEntity = true
}

sealed trait HasMany[+E <: EntityWithId] {
  def ids: List[E#ID]

  def entities: List[E]

  def isIds: Boolean

  def isEntities: Boolean
}

object HasMany {

  object None extends HasMany[Nothing] {
    val ids = Nil
    val entities = Nil
    val isEntities = false
    val isIds = false
  }

  def apply[E <: EntityWithId](ids: List[E#ID]) = HasIds[E](ids)

  def apply[E <: EntityWithId](entities: List[E]) = HasEntities[E](entities)

  implicit def fromEntity[E <: EntityWithId](entities: List[E]): HasEntities[E] = apply(entities)

  implicit def fromId[E <: EntityWithId](ids: List[E#ID]): HasIds[E] = apply(ids)

  implicit def HasWrites[E <: EntityWithId](implicit format: Writes[E], idFormat: Writes[E#ID]): Writes[HasMany[E]] = Writes { o =>
    if (o.isEntities) Writes.traversableWrites[E].writes(o.entities)
    else Writes.traversableWrites[E#ID].writes(o.ids)
  }

  implicit def HasReads[E <: EntityWithId](implicit format: Reads[E], idFormat: Reads[E#ID]): Reads[HasMany[E]] = Reads {
    case JsArray(elems) if elems.isEmpty => JsSuccess(HasMany.None)
    case json => Reads.traversableReads[List, E#ID].reads(json).map(id => HasIds(id)).orElse {
      Reads.traversableReads[List, E].reads(json).map(e => HasEntities(e))
    }
  }
}

final case class HasIds[E <: EntityWithId](ids: List[E#ID]) extends HasMany[E] {
  def entities = throw new NoSuchElementException("Has.entity on HasId")

  val isIds = true
  val isEntities = false
}

final case class HasEntities[E <: EntityWithId](entities: List[E]) extends HasMany[E] {
  lazy val ids = entities.map(_.id)
  val isIds = true
  val isEntities = true
}