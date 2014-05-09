name := "computer-database"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "qizero" %% "qizero-persistence" % "0.1.0-SNAPSHOT",
  "qizero" %% "qizero-service" % "0.1.0-SNAPSHOT",
  "qizero" %% "qizero-testkit" % "0.1.0-SNAPSHOT" % "test"
)

lazy val root = project in file(".")

root.enablePlugins(play.PlayScala)