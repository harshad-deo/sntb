package sntbtests

import sntb.TestProvider
import util.Try

class AssertTypeErrorTester extends TestProvider {

  it should "pass assertTypeError success" in {
    assertTypeError("implicitly[Numeric[String]]")
    assertTypeError("4.55 === true")
  }

  it should "pass assertTypeError failure" in {
    assert(Try(assertTypeError("implicitly[Numeric[Int]]")).isFailure)
    assert(Try(assertTypeError("implicitly[Numeric[Int]")).isFailure)
    assert(Try(assertTypeError("implicitly[Numer ic[String]]")).isFailure)
  }

}