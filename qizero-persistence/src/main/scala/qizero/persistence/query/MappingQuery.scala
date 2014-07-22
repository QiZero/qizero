package qizero.persistence.query

import qizero.action.Action
import qizero.domain.Page
import qizero.persistence.mapping.Mapper

trait MappingQuery {

  implicit final class MapperAction[R](action: Action[R]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(mapper)
  }

  implicit final class OptionMapperAction[R](action: Action[Option[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(_.map(mapper))
  }

  implicit final class SeqMapperAction[R](action: Action[Seq[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(_.map(mapper))
  }

  implicit final class PageMapperAction[R](action: Action[Page[R]]) {
    def as[E](implicit mapper: Mapper[R, E]) = action.map(_.map(mapper))
  }

}
