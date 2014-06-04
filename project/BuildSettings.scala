import sbt.Keys._

object BuildSettings {

  val basicSettings = Seq(
    organization := "qizero",
    scalaVersion := "2.11.1",
    crossScalaVersions := Seq("2.11.1", "2.10.4"),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-target:jvm-1.7"
    ),
    javacOptions ++= Seq(
      "-source", "1.7",
      "-target", "1.7"
    )
  ) ++ Publish.settings ++ Release.settings

  object Publish {

    val settings = Seq(
      publishMavenStyle := true
    )

    val noPublishing = Seq(
      publish := {},
      publishLocal := {},
      publishArtifact := false
    )
  }

  object Release {
    val settings = sbtrelease.ReleasePlugin.releaseSettings
  }

}
