package sntbtests

import sntb.TestProvider
import util.Try

class InterceptTester extends TestProvider {

  it should "pass successful intercept tests" in {
    intercept[IllegalArgumentException] {
      throw new IllegalArgumentException("example 1")
    }
    intercept[ArithmeticException] {
      0 / 0
    }
    intercept[NoSuchElementException] {
      Nil.head
    }
    intercept[UnsupportedOperationException] {
      Nil.tail
    }
  }

  it should "pass intercept failure tests" in {
    assert(Try(intercept[ArithmeticException](throw new IllegalArgumentException("example 1"))).isFailure)
    assert(Try(intercept[NoSuchElementException](throw new IllegalArgumentException("example 1"))).isFailure)
    assert(Try(intercept[UnsupportedOperationException](throw new IllegalArgumentException("example 1"))).isFailure)

    assert(Try(intercept[IllegalArgumentException](0 / 0)).isFailure)
    assert(Try(intercept[NoSuchElementException](0 / 0)).isFailure)
    assert(Try(intercept[UnsupportedOperationException](0 / 0)).isFailure)

    assert(Try(intercept[IllegalArgumentException](Nil.head)).isFailure)
    assert(Try(intercept[ArithmeticException](Nil.head)).isFailure)
    assert(Try(intercept[UnsupportedOperationException](Nil.head)).isFailure)

    assert(Try(intercept[IllegalArgumentException](Nil.tail)).isFailure)
    assert(Try(intercept[ArithmeticException](Nil.tail)).isFailure)
    assert(Try(intercept[NoSuchElementException](Nil.tail)).isFailure)
  }

}
