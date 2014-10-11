package qizero.persistence.query

import scala.language.higherKinds
import scala.slick.lifted.{Query, RunnableCompiled}
import qizero.action.db._
import qizero.entity.Mapper
import qizero.model._
import qizero.persistence.DAL

trait Finder[R] {
  def first: DBAction[R]
  def one: DBAction[Option[R]]
  def all: DBAction[List[R]]
  def page(pagination: Pagination): DBAction[Page[R]]
  def slice(pagination: Pagination): DBAction[Slice[R]]
  def count: DBAction[Int]
  def exists: DBAction[Boolean]

  final def as[T](implicit mapper: Mapper[R, T]): Finder[T] = new MappedFinder[R, T](this, mapper)
  final def map[B](f: R => B): Finder[B] = as(Mapper(f))
}

private final class QueriedFinder[R](query: Query[_, R, Seq])(implicit dal: DAL) extends Finder[R] {
  def first = new QueriedFindFirst(query)
  def one = new QueriedFindOne(query)
  def all = new QueriedFindAll(query)
  def page(pagination: Pagination) = new QueriedFindPage(query, pagination)
  def slice(pagination: Pagination) = new QueriedFindSlice(query, pagination)
  def count = new QueriedFindCount[R](query)
  def exists = new QueriedFindExists[R](query)
}

private final class CompiledFinder[R, C[_]](compiled: RunnableCompiled[_ <: Query[_, R, C], C[R]])(implicit dal: DAL) extends Finder[R] {
  def first = new CompiledFindFirst(compiled)
  def one = new CompiledFindOne(compiled)
  def all = new CompiledFindAll(compiled)
  def page(pagination: Pagination) = new CompiledFindPage(compiled, pagination)
  def slice(pagination: Pagination) = new CompiledFindSlice(compiled, pagination)
  def count = new CompiledFindCount(compiled)
  def exists = new CompiledFindExists(compiled)
}

private final class MappedFinder[R, E](finder: Finder[R], mapper: Mapper[R, E]) extends Finder[E] {
  def first = new MapDBAction(finder.first, mapper)
  def one = new MapDBAction(finder.one, mapper)
  def all = new MapDBAction(finder.all, mapper)
  def page(pagination: Pagination) = new MapDBAction(finder.page(pagination), mapper)
  def slice(pagination: Pagination) = new MapDBAction(finder.slice(pagination), mapper)
  def count = finder.count
  def exists = finder.exists
}