package mylogic

import cats.data.Validated
import cats.effect.IO
import cats.implicits._
import mylogic.Validators.{ValidationError, _}
import mymodel._
import org.slf4j.LoggerFactory
import mylogic.Validators.{ArrayElementError, ArrayLengthError, TargetValueError, ValidationError, validateArrayElements, validateArrayLength, validateTarget}

import scala.collection.mutable.ListBuffer

class MyService {

  private val logger = LoggerFactory.getLogger(getClass)

  private val config = ConfigLoader.config

  val defaultTarget = config.defaultTarget.toIntOption.getOrElse(42)
  val maxRequestsPerMinute = config.maxRequestsPerMinute.toIntOption.getOrElse(5)
  val timeWindowMillis = config.timeWindowMillis.toLongOption.getOrElse("6000".toLong)

  val rateLimiter = RateLimiter(maxRequestsPerMinute, timeWindowMillis)

  val results = new ListBuffer[(MyRequest, MyResponse)]()

  def getIndex(reqIdx: RequestIndex): IO[Either[ErrorInfo, ResponseIndex]] =
    IO {

      logger.info(s"Request index operation: ${reqIdx.toString}  ")

      val errors = validateReqIdx(reqIdx)

      if (errors.isInvalid) {
        val errorStr = errors.fold(
          errors => errors.map {
            case ArrayLengthError(actual, min, max) =>
              s"Array length must be between $min and $max, but was $actual"
            case ArrayElementError(value, min, max) =>
              s"Array contains invalid element $value. Values must be between $min and $max"
            case TargetValueError(value, min, max) =>
              s"Target value $value is invalid. Must be between $min and $max"
          }.mkString("\n"),
          _ => "" // This won't be used since we're checking isInvalid
        )
        logger.error(s"Errors during request index operation: $errorStr")
        Left(ErrorInfo(errorStr))

      } else {
        val data = reqIdx.data

        val resp = ResponseIndex(
          data.indices.flatMap { i =>
            (i + 1 until data.length)
              .filter(j => data(i) + data(j) == reqIdx.target)
              .map(j => Pair((i, j), (data(i), data(j))))
          }.toList)

        addToResults(reqIdx, resp)
        logger.info(s"Result of processing request index operation: ${resp.toString}")
        Right(resp)
      }
    }

  def getTarget(req: RequestTarget): IO[Either[ErrorInfo, ResponseTarget]] =
    IO {
      logger.info(s"Get target operation: ${req.toString}")
      val resp = ResponseTarget(req.target.getOrElse(defaultTarget))
      addToResults(req, resp)
      logger.info(s"Result of get target operation: ${resp.toString} ")
      Right(resp)
    }

  def find(clientIdOpt: Option[String], req: RequestFind): IO[Either[ErrorInfo, ResponseFind]] =
    IO {

      logger.info(s"Find operation request: ${req.toString}")

      val clientId = clientIdOpt.getOrElse("anonymous")

      if (rateLimiter.isAllowed(clientId)) {

        val filtRes = results.filter(v => v._1 match {
          case ri: RequestIndex => ri.data.exists(d => req.data == d) ||
            req.target.map(t => t == ri.target).getOrElse(false)
          case rt: RequestTarget => rt.target == req.target
        }
        )
        val res = ResponseFind(filtRes.toList.map(_._2))
        logger.info(s"Result of find opertion: ${res.toString}")
        Right(res)
      } else {
        val sErr = s"Rate limit exceeded. Maximum $maxRequestsPerMinute requests per minute allowed."
        logger.error(sErr)
        Left(ErrorInfo(sErr))
      }

    }


  private def addToResults(req: MyRequest, resp: MyResponse): Unit =
    results += Tuple2(req, resp)

  private def validateReqIdx(reqIdx: RequestIndex): Validated[List[ValidationError], RequestIndex] = {
    (
      validateArrayLength(reqIdx.data).toValidatedNel, // Convert to ValidatedNel
      validateArrayElements(reqIdx.data).toValidatedNel, // Convert to ValidatedNel
      validateTarget(reqIdx.target).toValidatedNel // Convert to ValidatedNel
    ).mapN { (_, _, _) => reqIdx } // Combine validations
      .leftMap(_.toList) // Convert NonEmptyList to List
  }

}

object MyService {
  def apply() = new MyService()
}
