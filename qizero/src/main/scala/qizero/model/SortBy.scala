package qizero.model

sealed trait SortBy

object SortBy {

  object Desc extends SortBy {

    object NullsFirst extends SortBy

    object NullsLast extends SortBy

  }

  object Asc extends SortBy {

    object NullsFirst extends SortBy

    object NullsLast extends SortBy

  }

}
