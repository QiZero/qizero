import sbt.Keys._

object BuildSettings {

  val basicSettings = Seq(
    organization := "qizero",
    scalaVersion := "2.11.0",
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
    ),
    testListeners += new eu.henkelmann.sbt.JUnitXmlTestsListener(target.value.getAbsolutePath)
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
