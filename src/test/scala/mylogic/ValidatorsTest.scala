package mylogic

import cats.data.Validated
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Validators.{ArrayLengthError, ArrayElementError, TargetValueError}

class ValidatorsTest extends AnyFlatSpec with Matchers {

  "Validators" should "validate array length correctly" in {
    val validArray    = Array(1, 2, 3)
    val tooShortArray = Array(1)
    val tooLongArray  = Array.fill(105)(1)

    Validators.validateArrayLength(validArray).isValid shouldBe true
    Validators.validateArrayLength(tooShortArray).isInvalid shouldBe true
    Validators.validateArrayLength(tooLongArray).isInvalid shouldBe true
  }

  it should "validate array elements correctly" in {
    val validArray   = Array(1, 2, 3)
    val invalidArray = Array(1, 2, 1_000_000_001)

    Validators.validateArrayElements(validArray).isValid shouldBe true
    Validators.validateArrayElements(invalidArray).isInvalid shouldBe true
  }

  it should "validate target value correctly" in {
    val validTarget   = 42
    val invalidTarget = 1_000_000_001

    Validators.validateTarget(validTarget).isValid shouldBe true
    Validators.validateTarget(invalidTarget).isInvalid shouldBe true
  }

  it should "provide correct error messages for array length" in {
    val tooShortArray = Array(1)
    Validators
      .validateArrayLength(tooShortArray)
      .fold(
        error => error shouldBe ArrayLengthError(1, Constraints.MIN_LENGTH, Constraints.MAX_LENGTH),
        _ => fail("Should have been invalid")
      )
  }

  it should "provide correct error messages for array elements" in {
    val invalidArray = Array(1, 2, 1_000_000_001)
    Validators
      .validateArrayElements(invalidArray)
      .fold(
        error => error shouldBe ArrayElementError(1_000_000_001, Constraints.MIN_VALUE, Constraints.MAX_VALUE),
        _ => fail("Should have been invalid")
      )
  }

  it should "provide correct error messages for target value" in {
    val invalidTarget = 1_000_000_001
    Validators
      .validateTarget(invalidTarget)
      .fold(
        error => error shouldBe TargetValueError(1_000_000_001, Constraints.MIN_VALUE, Constraints.MAX_VALUE),
        _ => fail("Should have been invalid")
      )
  }
}
 