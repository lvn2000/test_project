import Dependencies.*

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.test"
ThisBuild / organizationName := "test"

lazy val root = (project in file("."))
  .settings(
    name := "test_project",
    libraryDependencies ++= Seq(munit % Test,
      tapir_core, tapir_json, tapir_server,
      https,
      circe_generic, cats_effect, conf, conf_cats,
      log,
      blemale)
  )

Compile / run / fork := true