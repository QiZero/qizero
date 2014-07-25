import sbt._

object Dependencies {

  val resolvers = Seq(
    Resolver.sonatypeRepo("releases"),
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  )
  val paradise = addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
  // Util
  private val jodaTime = "joda-time" % "joda-time" % "2.3"
  private val jodaConvert = "org.joda" % "joda-convert" % "1.4"

  // Logging
  private val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"
  private val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

  // Config
  private val config = "com.typesafe" % "config" % "1.2.1"

  // Persistence
  private val slick = "com.typesafe.slick" %% "slick" % "2.1.0-RC2"
  private val h2 = "com.h2database" % "h2" % "1.3.176"

  // Play
  private val playVersion = "2.3.2"
  private val playJson = "com.typesafe.play" %% "play-json" % playVersion

  // Akka
  private val akkaVersion = "2.3.4"
  private val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  private val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  private val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion

  // Test
  private val scalatest = "org.scalatest" %% "scalatest" % "2.2.0"
  private val mockito = "org.mockito" % "mockito-core" % "1.9.5"

  // -------------------------------------------------------------------------------------------------------------------
  // Module Dependecies
  // -------------------------------------------------------------------------------------------------------------------
  val testDeps = test(scalatest, mockito)
  val annotationDeps = provided(playJson) ++ testDeps
  val loggingDeps = compile(slf4j) ++ testDeps
  val configDeps = compile(config) ++ testDeps
  val actionDeps = compile() ++ testDeps
  val persistenceDeps = provided(slick) ++ compile(jodaTime, jodaConvert) ++ test(h2) ++ testDeps
  val serviceDeps = provided(akka) ++ testDeps
  val testkitDeps = optional(slick, akka) ++ compile(scalatest, akkaTestkit, h2)

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
