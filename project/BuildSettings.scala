import sbt.Keys._
import sbt._

object BuildSettings {

  object Basic {
    val settings = Seq(
      organization := "qizero",
      scalaVersion := "2.12.13",
      resolvers ++= Dependencies.resolvers,
      scalacOptions ++= Seq(
        "-encoding", "UTF-8",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-target:jvm-1.8"
      ),
      javacOptions ++= Seq(
        "-source", "1.8",
        "-target", "1.8"
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
