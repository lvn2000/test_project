import cats.effect._
import myendpoint.MainEndpoint
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._

object MyMain extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(MainEndpoint.helloRoutes.orNotFound)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
