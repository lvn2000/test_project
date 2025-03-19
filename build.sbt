import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.test"
ThisBuild / organizationName := "test"

lazy val root = (project in file("."))
  .settings(
    name := "test_project",
    libraryDependencies ++= Seq(munit % Test,
                                tapir_core, tapir_json, tapir_server,
                                https,
                                circe_generic, cats_effect,
                                log)
)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
