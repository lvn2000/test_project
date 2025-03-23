import Dependencies.*

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.test"
ThisBuild / organizationName := "test"

javacOptions ++= Seq("-source", "1.17", "-target", "1.17")

lazy val root = (project in file("."))
  .settings(
    name := "test_project",
    libraryDependencies ++= Seq(
      tapir_core, tapir_json, tapir_server,
      https,
      circe_generic, cats_effect, conf, conf_cats,
      log,
      blemale)
  )

Compile / run / fork := true

assembly / assemblyJarName := "test_project.jar"

/*  for excluding problem in assembling
Deduplicate found different file contents in the following:
Jar name = logback-classic-1.4.7.jar, jar org = ch.qos.logback, entry target = module-info.class
Jar name = logback-core-1.4.7.jar, jar org = ch.qos.logback, entry target = module-info.class
Jar name = caffeine-3.1.8.jar, jar org = com.github.ben-manes.caffeine, entry target = module-info.class
 */
assemblyMergeStrategy in assembly := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in assembly := Some("MyMain")

enablePlugins(JavaAppPackaging, DockerPlugin)
enablePlugins(DockerPlugin)

// Docker specific settings
Docker / packageName := "test_project"
Docker / version := "latest"
dockerBaseImage := "openjdk:24-ea-jdk-slim"
Docker / maintainer := "vlad_lubenchenko@hotmail.com"
Docker / dockerExposedPorts := Seq(8080)
