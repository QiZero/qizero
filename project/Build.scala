import sbt.Keys._
import sbt._

object Build extends Build {

  import BuildSettings._
  import Dependencies._

  // -------------------------------------------------------------------------------------------------------------------
  // Modules
  // -------------------------------------------------------------------------------------------------------------------
  val qizero = Project("qizero", file("qizero"))
    .settings(Basic.settings: _*)
    .settings(
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _),
      libraryDependencies ++= provided(slick, Play.json, Akka.actor),
      libraryDependencies ++= compile(slf4j, config, jodaTime, jodaConvert),
      libraryDependencies ++= test(scalatest, scalamock, h2)
    )

  val testkit = Project("qizero-testkit", file("qizero-testkit"))
    .dependsOn(qizero)
    .settings(Basic.settings: _*)
    .settings(
      libraryDependencies ++= optional(slick, Play.json),
      libraryDependencies ++= compile(scalatest, Akka.testkit, h2)
    )
  // -------------------------------------------------------------------------------------------------------------------
  // Root
  // -------------------------------------------------------------------------------------------------------------------
  val root = Project("root", file("."))
    .aggregate(qizero, testkit)
    .settings(Basic.settings: _*)
    .settings(Publish.noPublishing: _*)

}