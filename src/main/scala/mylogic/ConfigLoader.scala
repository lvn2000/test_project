package mylogic

import pureconfig._
import pureconfig.generic.auto._

case class AppConfig(defaultTarget: String, maxRequestsPerMinute: String, timeWindowMillis:String)

object ConfigLoader {
  // Load configuration once and make it available
  val config: AppConfig = ConfigSource.default.load[AppConfig] match {
    case Right(conf) => conf
    case Left(failures) =>
      println(s"Failed to load configuration: ${failures.toList.mkString(", ")}")
      throw new RuntimeException("Failed to load configuration")
  }

}
