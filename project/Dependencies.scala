import sbt._

object Dependencies {
  val tapir_version = "1.2.9"
  val tapir_core    = "com.softwaremill.sttp.tapir" %% "tapir-core"          % tapir_version
  val tapir_json    = "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % tapir_version
  val tapir_server  = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapir_version

  val https = "org.http4s" %% "http4s-blaze-server" % "0.23.12"

  val circe_generic = "io.circe" %% "circe-generic" % "0.14.3"

  val cats_effect = "org.typelevel" %% "cats-effect" % "3.4.8"

  val log = "ch.qos.logback" % "logback-classic" % "1.4.7"

  val blemale = "com.github.blemale" %% "scaffeine" % "5.3.0"

  val pureconfVersion = "0.17.8"
  val conf            = "com.github.pureconfig" %% "pureconfig"             % pureconfVersion
  val conf_cats       = "com.github.pureconfig" %% "pureconfig-cats-effect" % pureconfVersion

  val testDep = "org.scalatest" %% "scalatest" % "3.2.15" % Test

}
