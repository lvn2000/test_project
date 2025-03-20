package myendpoint

import cats.effect._
import io.circe.generic.auto._
import mylogic.MyService
import mymodel._
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

object MainEndpoint {

  val service = MyService()

  //Route for evaluating get index
  //example cUrl:
  // curl --request POST \
  //  --url http://localhost:8080/getindex \
  //  --header 'Content-Type: application/json' \
  //  --header 'User-Agent: insomnia/10.3.0' \
  //  --data '{
  //	"data":[3,8,10,14],
  //  "target":18
  //}'
  val getIndex: ServerEndpoint[Any, IO] = endpoint.post
    .in("getindex")
    .in(jsonBody[RequestIndex])
    .errorOut(statusCode(StatusCode.BadRequest).and(jsonBody[ErrorInfo]))
    .out(statusCode(StatusCode.Ok).and(jsonBody[ResponseIndex]))
    .serverLogic { request => service.getIndex(request) }

  //Route for evaluating get target
  val getTarget: ServerEndpoint[Any, IO] = endpoint.get
    .in("target")
    .in(jsonBody[RequestTarget])
    .errorOut(statusCode(StatusCode.BadRequest).and(jsonBody[ErrorInfo]))
    .out(statusCode(StatusCode.Ok).and(jsonBody[ResponseTarget]))
    .serverLogic { request => service.getTarget(request) }

  //Route for evaluating find
  //  Example:
  //  curl --request POST \
  //  --url http://localhost:8080/find \
  //    --header 'Content-Type: application/json' \
  //  --header 'User-Agent: insomnia/10.3.0' \
  //  --data '{
  //    "data":[3,8,10,14],
  //    "target":18
  //  }'
  val find: ServerEndpoint[Any, IO] = endpoint.post
    .in("find")
    .in(jsonBody[RequestFind])
    .in(header[Option[String]]("X-Client-ID"))
    .errorOut(statusCode(StatusCode.BadRequest).and(jsonBody[ErrorInfo]))
    .out(statusCode(StatusCode.Ok).and(jsonBody[ResponseFind]))
    .serverLogic { case (request, clientId) => service.find(clientId, request) }

  // routes
  val helloRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(List(getIndex, getTarget, find))
}
