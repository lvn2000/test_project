package mylogic

import cats.data.Validated

object Validators {

  import Constraints._

  // Define validation errors
  sealed trait ValidationError

  case class ArrayLengthError(actual: Int, min: Int, max: Int) extends ValidationError

  case class ArrayElementError(value: Int, min: Int, max: Int) extends ValidationError

  case class TargetValueError(value: Int, min: Int, max: Int) extends ValidationError

  // Validator for the array length
  def validateArrayLength(nums: Array[Int]): Validated[ArrayLengthError, Array[Int]] =
    if (nums.length >= MIN_LENGTH && nums.length <= MAX_LENGTH) {
      Validated.valid(nums)
    } else {
      Validated.invalid(ArrayLengthError(nums.length, MIN_LENGTH, MAX_LENGTH))
    }

  // Validator for array elements
  def validateArrayElements(nums: Array[Int]): Validated[ArrayElementError, Array[Int]] =
    nums.find(num => num < MIN_VALUE || num > MAX_VALUE) match {
      case Some(invalidValue) => Validated.invalid(ArrayElementError(invalidValue, MIN_VALUE, MAX_VALUE))
      case None               => Validated.valid(nums)
    }

  // Validator for target value
  def validateTarget(target: Int): Validated[TargetValueError, Int] =
    if (target >= MIN_VALUE && target <= MAX_VALUE) {
      Validated.valid(target)
    } else {
      Validated.invalid(TargetValueError(target, MIN_VALUE, MAX_VALUE))
    }

}

object Constraints {
  val MIN_LENGTH = 2
  val MAX_LENGTH = 104
  val MIN_VALUE  = -1_000_000_000 // -10^9
  val MAX_VALUE  = 1_000_000_000  // 10^9
}
