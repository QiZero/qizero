import sbt._

object Dependencies {

  val resolvers = Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.typesafeRepo("releases")
  )
  // Util
  val jodaTime = "joda-time" % "joda-time" % "2.6"
  val jodaConvert = "org.joda" % "joda-convert" % "1.7"

  // Logging
  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.9"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.2"

  // Config
  val config = "com.typesafe" % "config" % "1.2.1"

  // Persistence
  val slick = "com.typesafe.slick" %% "slick" % "3.0.0-M1"
  val h2 = "com.h2database" % "h2" % "1.3.176"

  // Play
  object Play {
    private val version = "2.3.7"
    val json = "com.typesafe.play" %% "play-json" % version
  }

  // Akka
  object Akka {
    private val version = "2.3.8"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
    val slf4j = "com.typesafe.akka" %% "akka-slf4j" % version
    val testkit = "com.typesafe.akka" %% "akka-testkit" % version
  }

  // Test
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.1"
  val scalamock = "org.scalamock" %% "scalamock-scalatest-support" % "3.2"

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
