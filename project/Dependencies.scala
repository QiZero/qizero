import sbt._

object Dependencies {

  val resolvers = Seq(
    Resolver.sonatypeRepo("releases"),
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  )
  // Util
  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val jodaConvert = "org.joda" % "joda-convert" % "1.4"

  // Logging
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.7"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

  // Config
  val config = "com.typesafe" % "config" % "1.2.1"

  // Persistence
  val slick = "com.typesafe.slick" %% "slick" % "2.1.0"
  val h2 = "com.h2database" % "h2" % "1.3.176"

  // Play
  val playVersion = "2.3.3"
  val playJson = "com.typesafe.play" %% "play-json" % playVersion

  // Akka
  val akkaVersion = "2.3.5"
  val akka = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion

  // Test
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.1"
  val mockito = "org.mockito" % "mockito-core" % "1.9.5"

  // -------------------------------------------------------------------------------------------------------------------
  // Utils
  // -------------------------------------------------------------------------------------------------------------------
  def compile(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "compile")
  def provided(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "provided")
  def optional(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "optional")
  def test(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "test")
  def runtime(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "runtime")
  def container(deps: ModuleID*): Seq[ModuleID] = deps.map(_ % "container")
}
