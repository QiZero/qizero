package qizero.persistence.mapper

import qizero.model.Page

trait Mapper[R, E] extends (R => E)

object Mapper {

  implicit final class OptionMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Option[R], Option[E]] {
    def apply(r: Option[R]): Option[E] = r.map(mapper)
  }

  implicit final class SeqMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Seq[R], Seq[E]] {
    def apply(r: Seq[R]): Seq[E] = r.map(mapper)
  }

  implicit final class PageMapper[R, E](mapper: Mapper[R, E]) extends Mapper[Page[R], Page[E]] {
    def apply(r: Page[R]): Page[E] = Page(r.content.map(mapper), r.pagination, r.total)
  }

}