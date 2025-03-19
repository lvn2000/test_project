package mylogic

import cats.effect.IO
import mymodel._

import scala.collection.mutable.ListBuffer

class MyService {

  val defaultTarget = 42

  val results = new ListBuffer[(MyRequest, MyResponse)]()

  def getIndex(reqIdx: RequestIndex): IO[Either[ErrorInfo, ResponseIndex]] =
    IO {
      if (reqIdx.data.isEmpty) {
        Left(ErrorInfo("Input array is empty"))
      } else {
        val data = reqIdx.data

        val resp = ResponseIndex(
          data.indices.flatMap { i =>
            (i + 1 until data.length)
              .filter(j => data(i) + data(j) == reqIdx.target)
              .map(j => Pair((i, j), (data(i), data(j))))
          }.toList)

        addToResults(reqIdx, resp)
        Right(resp)
      }
    }

  def getTarget(req: RequestTarget): IO[Either[ErrorInfo, ResponseTarget]] =
    IO {
      val resp = ResponseTarget(req.target.getOrElse(defaultTarget))
      addToResults(req, resp)
      Right(resp)
    }

  def find(req: RequestFind): IO[Either[ErrorInfo, ResponseFind]] =
    IO {
      val filtRes = results.filter(v => v._1 match {
        case ri: RequestIndex => ri.data.exists(d => req.data == d) ||
          req.target.map(t => t == ri.target).getOrElse(false)
        case rt: RequestTarget => rt.target == req.target
      }
      )

      Right(ResponseFind(filtRes.toList.map(_._2)))
    }


  private def addToResults(req: MyRequest, resp: MyResponse): Unit =
    results += Tuple2(req, resp)
}

object MyService {
  def apply() = new MyService()
}
