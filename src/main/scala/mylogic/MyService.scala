package mylogic

import cats.data.Validated
import cats.effect.IO
import cats.implicits._
import mymodel._
import mylogic.Validators.{ArrayElementError, ArrayLengthError, TargetValueError, validateArrayElements, validateArrayLength, validateTarget, ValidationError}

import scala.collection.mutable.ListBuffer

class MyService {

  val defaultTarget = 42

  val results = new ListBuffer[(MyRequest, MyResponse)]()

  def getIndex(reqIdx: RequestIndex): IO[Either[ErrorInfo, ResponseIndex]] =
    IO {
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

  private def validateReqIdx(reqIdx: RequestIndex): Validated[List[ValidationError], RequestIndex] = {
    (
      validateArrayLength(reqIdx.data).toValidatedNel, // Convert to ValidatedNel
      validateArrayElements(reqIdx.data).toValidatedNel, // Convert to ValidatedNel
      validateTarget(reqIdx.target).toValidatedNel // Convert to ValidatedNel
    ).mapN { (_, _, _) => reqIdx } // Combine validations
      .leftMap(_.toList) // Convert NonEmptyList to List
  }

  // Pretty printing of validation errors
  //def formatErrors(errors: List[ValidationError]): String = {
  //  errors.map {
  //    case ArrayLengthError(actual, min, max) =>
  //      s"Array length must be between $min and $max, but was $actual"
  //    case ArrayElementError(value, min, max) =>
  //      s"Array contains invalid element $value. Values must be between $min and $max"
  //    case TargetValueError(value, min, max) =>
  //      s"Target value $value is invalid. Must be between $min and $max"
  //  }.mkString("\n")
  //  }

}

object MyService {
  def apply() = new MyService()
}
