import sbt.Keys._
import sbt._

object Build extends Build {

  import BuildSettings._
  import Dependencies._

  val qizero = Project("qizero", file("qizero"))
    .settings(Basic.settings: _*)
    .settings(
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _),
      libraryDependencies ++= provided(slick, playJson),
      libraryDependencies ++= compile(slf4j, config, jodaTime, jodaConvert),
      libraryDependencies ++= test(scalatest, mockito, h2)
    )

  val testkit = Project("qizero-testkit", file("qizero-testkit"))
    .dependsOn(qizero)
    .settings(Basic.settings: _*)
    .settings(
      libraryDependencies ++= provided(slick, playJson),
      libraryDependencies ++= compile(scalatest, akkaTestkit, h2)
    )
  // -------------------------------------------------------------------------------------------------------------------
  // Modules
  // -------------------------------------------------------------------------------------------------------------------
  //  val json = module("qizero-json")
  //    .settings(
  //      paradise,
  //      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
  //      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _),
  //      libraryDependencies ++= jsonDeps
  //    )
  //
  //  val logging = module("qizero-logging")
  //    .settings(libraryDependencies ++= loggingDeps)
  //
  //  val config = module("qizero-config")
  //    .settings(libraryDependencies ++= configDeps)
  //
  //  val i18n = module("qizero-i18n")
  //
  //  val action = module("qizero-action")
  //    .dependsOn(logging)
  //    .settings(libraryDependencies ++= actionDeps)
  //
  //  val persistence = module("qizero-persistence")
  //    .dependsOn(logging, config, action)
  //    .settings(
  //      libraryDependencies ++= persistenceDeps,
  //      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _)
  //    )

  //  val persistenceMapper = module("qizero-persistence-mapper")
  //    .settings(
  //      libraryDependencies ++= persistenceDeps,
  //      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _),
  //      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _)
  //    )

  //  val service = module("qizero-service")
  //    .dependsOn(action)
  //    .settings(libraryDependencies ++= serviceDeps)
  //

  //
  //  val all = module("qizero-all")
  //    .dependsOn(logging, config, i18n, action, persistence, service)
  // -------------------------------------------------------------------------------------------------------------------
  // Root
  // -------------------------------------------------------------------------------------------------------------------
  val root = Project("root", file("."))
    .aggregate(qizero, testkit)
    .settings(Basic.settings: _*)
    .settings(Publish.noPublishing: _*)
  // -------------------------------------------------------------------------------------------------------------------
  // Utils
  // -------------------------------------------------------------------------------------------------------------------
  //  private def base(project: Project) = project.settings(Basic.settings: _*)
  //  private def module(name: String, path: String = ".") = base(Project(id = name, base = file(s"$path/$name")))
}