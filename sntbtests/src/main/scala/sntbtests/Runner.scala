package sntbtests

object Runner {

  private val elems = List(new AssertCompilesTester, new AssertTypeErrorTester, new InterceptTester)

  def main(args: Array[String]): Unit = {
    elems foreach (_.run)
  }
}