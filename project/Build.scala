import sbt.Keys._
import sbt._

object Build extends Build {

  import BuildSettings._
  import Dependencies._

  // -------------------------------------------------------------------------------------------------------------------
  // Modules
  // -------------------------------------------------------------------------------------------------------------------
  val logging = module("qizero-logging")
    .settings(libraryDependencies ++= loggingDeps)

  val config = module("qizero-config")
    .settings(libraryDependencies ++= configDeps)

  val i18n = module("qizero-i18n")

  val action = module("qizero-action")
    .dependsOn(logging)
    .settings(libraryDependencies ++= actionDeps)

  val persistence = module("qizero-persistence")
    .dependsOn(logging, config, action)
    .settings(libraryDependencies ++= persistenceDeps)

  val service = module("qizero-service")
    .dependsOn(action)
    .settings(libraryDependencies ++= serviceDeps)

  val testkit = module("qizero-testkit")
    .dependsOn(action, persistence, service)
    .settings(libraryDependencies ++= testkitDeps)
  // -------------------------------------------------------------------------------------------------------------------
  // Utils
  // -------------------------------------------------------------------------------------------------------------------
  override val settings = super.settings ++ Seq(
    shellPrompt := (s => "[" + Project.extract(s).currentProject.id + "] $ ")
  )
  private def base(project: Project) = project.settings(basicSettings: _*)
  private def module(name: String, path: String = ".") = base(Project(id = name, base = file(s"$path/$name")))
}