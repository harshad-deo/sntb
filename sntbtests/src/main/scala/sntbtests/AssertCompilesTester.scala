package sntbtests

import sntb.TestProvider
import util.Try

class AssertCompilesTester extends TestProvider {

  class Foo
  object Bar {
    class Baz
  }
  val foo = 42
  final val bar = 21
  lazy val baz = 11.0
  def quux = 22

  it should "pass assertCompiles success" in {
    assertCompiles("val a = 42")
    assertCompiles("implicitly[Numeric[Int]]")
    assertCompiles("new Foo")
    assertCompiles("new Bar.Baz")
    assertCompiles("foo + 11")
    assertCompiles("bar + 3.14")
    assertCompiles("baz * 2")
    assertCompiles("quux / 2")
    assertCompiles("4.55 === 4.55")
  }

  it should "pass assertCompiles failure" in {
    assert(Try(assertCompiles("a = 42")).isFailure)
    assert(Try(assertCompiles("implicitly[Numeric[String]]")).isFailure)
    assert(Try(assertCompiles("implicitly[Numer ic[Int]]")).isFailure)
  }

}
