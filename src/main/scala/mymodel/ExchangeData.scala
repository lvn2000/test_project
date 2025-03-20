package mymodel

sealed trait MyRequest

sealed trait MyResponse

case class Pair(indices: (Int, Int), numbers: (Int, Int))

case class RequestIndex(data: Array[Int], target: Int) extends MyRequest

case class ResponseIndex(pair: List[Pair]) extends MyResponse

case class RequestTarget(target: Option[Int]) extends MyRequest

case class ResponseTarget(target: Int) extends MyResponse

case class RequestFind(data: Int, target: Option[Int])

case class ResponseFind(result: List[MyResponse])

case class ErrorInfo(message: String)


sealed trait ValidationError

case class ArrayLengthError(actual: Int, min: Int, max: Int) extends ValidationError

case class ArrayElementError(value: Int, min: Int, max: Int) extends ValidationError

case class TargetValueError(value: Int, min: Int, max: Int) extends ValidationError
// Define the domain model

