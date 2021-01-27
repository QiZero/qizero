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
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
      libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      libraryDependencies ++= provided(slick, playJson, akka),
      libraryDependencies ++= compile(slf4j, config, jodaTime, jodaConvert),
      libraryDependencies ++= test(scalatest, mockito, h2)
    )

  // val testkit = Project("qizero-testkit", file("qizero-testkit"))
    // .dependsOn(qizero)
    // .settings(Basic.settings: _*)
    // .settings(
      // libraryDependencies ++= optional(slick, playJson),
      // libraryDependencies ++= compile(scalatest, akkaTestkit, h2)
    // )
  // -------------------------------------------------------------------------------------------------------------------
  // Root
  // -------------------------------------------------------------------------------------------------------------------
  val root = Project("root", file("."))
    .aggregate(
      qizero//, 
      // testkit
    )
    .settings(Basic.settings: _*)
    .settings(Publish.noPublishing: _*)

}
