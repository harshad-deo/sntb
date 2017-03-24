package simianquant.sntbtests

object Runner {

  private val elems = List(new AssertCompilesTester, new AssertTypeErrorTester)

  def main(args: Array[String]): Unit = {
    elems foreach (_.run)
  }
}