import sbt.Keys._
import sbt._

object Build extends Build {

  import BuildSettings._
  import Dependencies._

  // -------------------------------------------------------------------------------------------------------------------
  // Modules
  // -------------------------------------------------------------------------------------------------------------------
  val logging = module("qizero-logging")
    .settings(
      libraryDependencies ++= loggingDeps
    )
  // -------------------------------------------------------------------------------------------------------------------
  // Utils
  // -------------------------------------------------------------------------------------------------------------------
  private def base(project: Project) = project.settings(basicSettings: _*)
  private def module(name: String, path: String = ".") = base(Project(id = name, base = file(s"$path/$name")))
}