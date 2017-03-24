package simianquant.sntbtests

import simianquant.sntb.TestProvider

object Tester extends TestProvider {

  def main(args: Array[String]): Unit = {
    assertTypeError("implicitly[Numeric[Str  ing]]")
    println("Tests Successful")
  }
}