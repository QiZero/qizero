import sbt._

object Dependencies {

  // Logging
  private val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"
  private val logback = "ch.qos.logback" % "logback-classic" % "1.0.13"
  // Config
  private val config = "com.typesafe" % "config" % "1.2.0"
  // Test
  private val scalatest = "org.scalatest" %% "scalatest" % "2.1.4"
  private val mockito = "org.mockito" % "mockito-core" % "1.9.5"

  // -------------------------------------------------------------------------------------------------------------------
  // Module Dependecies
  // -------------------------------------------------------------------------------------------------------------------
  val testDeps = test(scalatest, mockito)
  val loggingDeps = compile(slf4j) ++ testDeps
  // -------------------------------------------------------------------------------------------------------------------
  // Utils
  // -------------------------------------------------------------------------------------------------------------------
  private def compile(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "compile")
  private def provided(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "provided")
  private def optional(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "optional")
  private def test(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "test")
  private def runtime(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "runtime")
  private def container(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "container")
}
