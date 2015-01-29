import sbt.Keys._
import sbt._

object BuildSettings {

  object Basic {
    val settings = Seq(
      organization := "qizero",
      scalaVersion := "2.11.5",
      resolvers ++= Dependencies.resolvers,
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
      shellPrompt := (s => "[" + Project.extract(s).currentProject.id + "] $ ")
    ) ++ Publish.settings ++ Release.settings
  }

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
