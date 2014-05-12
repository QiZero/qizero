import sbt._

object Dependencies {

  // Util
  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val jodaConvert = "org.joda" % "joda-convert" % "1.4"

  // Logging
  private val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"
  private val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

  // Config
  private val config = "com.typesafe" % "config" % "1.2.0"

  // Persistence
  private val slick = "com.typesafe.slick" %% "slick" % "2.1.0-M1" cross CrossVersion.binaryMapped{
    case "2.11" => "2.11.0-RC4"
    case v => v
  }
  private val h2 = "com.h2database" % "h2" % "1.3.175"

  // Akka
  private val akkaVersion = "2.3.2"
  private val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  private val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion

  // Test
  private val scalatest = "org.scalatest" %% "scalatest" % "2.1.4"
  private val mockito = "org.mockito" % "mockito-core" % "1.9.5"

  // -------------------------------------------------------------------------------------------------------------------
  // Module Dependecies
  // -------------------------------------------------------------------------------------------------------------------
  val testDeps = test(scalatest, mockito)
  val loggingDeps = compile(slf4j) ++ testDeps
  val configDeps = compile(config) ++ testDeps
  val actionDeps = compile() ++ testDeps
  val persistenceDeps = compile(jodaTime, jodaConvert, slick) ++ test(h2) ++ testDeps
  val serviceDeps = compile(akka) ++ testDeps
  val testkitDeps = compile(scalatest, akkaTestkit, h2)

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
