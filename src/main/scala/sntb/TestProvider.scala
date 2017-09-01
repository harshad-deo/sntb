package sntb

import language.experimental.macros
import language.implicitConversions
import reflect.ClassTag
import reflect.macros.blackbox.Context
import reflect.macros.{ParseException, TypecheckException}
import util.{Failure, Success, Try}

abstract class TestProvider {

  private var runList: List[Block] = Nil

  private val className = this.getClass.getSimpleName()

  protected def assertCompiles(str: String): Unit = macro TestProvider.assertCompilesImpl

  protected def assertTypeError(str: String): Unit = macro TestProvider.assertTypeErrorImpl

  protected object it {
    def should(name: String): BlockBuilder = BlockBuilder(name)
  }

  protected def intercept[T <: RuntimeException](arg: => Any)(implicit ct: ClassTag[T]): T =
    try {
      arg
      throw new RuntimeException("Value was not an error")
    } catch {
      case x: T => x
      case _: Throwable => throw new RuntimeException(s"Error was not an instance of the expected type")
    }

  final def run(): Unit = {
    println(s"${Console.GREEN}$className: ${Console.RESET}")
    runList.reverse foreach {
      case Block(name, exec) =>
        print(s"- $name...")
        val initTime = System.currentTimeMillis
        exec()
        val endTime = System.currentTimeMillis
        println(s"done (${TestProvider.formatTime(endTime - initTime)}) ")
    }
  }

  protected case class BlockBuilder(name: String) {
    def in(exec: => Unit): Unit = runList = new Block(name, () => exec) :: runList
  }

  private case class Block(name: String, exec: Function0[Unit])

  protected final class TSEQ[T](lhs: T) {
    def ===(rhs: T): Boolean = lhs == rhs
  }

  protected implicit def tseqBuilder[T](lhs: T): TSEQ[T] = new TSEQ(lhs)

}

object TestProvider {

  private def formatTime(millis: Long): String = {
    var result = ""
    var opr = millis
    val min = opr / 60000l
    if (min > 0) {
      if (min == 1) {
        result = "1 minute"
      } else {
        result = s"$min minutes"
      }
    }
    opr %= 60000
    val sec = opr / 1000l
    if (sec > 0) {
      val spacer = if (result.isEmpty) "" else " "
      if (sec == 1) {
        result += s"${spacer}1 second"
      } else {
        result += s"$spacer$sec seconds"
      }
    }
    opr %= 1000
    val spacer = if (result.isEmpty) "" else " "
    if (opr == 1) {
      result + s"${spacer}1 millisecond"
    } else {
      result + s"${spacer}$opr milliseconds"
    }
  }

  def assertCompilesImpl(c: Context)(str: c.Tree): c.Tree = {
    import c.universe._
    str match {
      case q"${y: String}" =>
        util.Try {
          val parsed = c.parse(y)
          val typechecked = c.typecheck(parsed)
          true
        } match {
          case util.Success(_) => q"assert(true)"
          case util.Failure(TypecheckException(_, msg)) => q"""assert(false, "typecheck failure: " + $msg)"""
          case util.Failure(ParseException(_, msg)) => q"""assert(false, "parse failure: " + $msg)"""
          case util.Failure(err) => q"assert(false, error: ${err.toString})"
        }
      case _ => c.abort(c.enclosingPosition, "Supplied Literal must be a string")
    }
  }

  def assertTypeErrorImpl(c: Context)(str: c.Tree): c.Tree = {
    import c.universe._
    str match {
      case q"${y: String}" =>
        util.Try {
          val parsed = c.parse(y)
          val typechecked = c.typecheck(parsed)
        } match {
          case util.Failure(TypecheckException(_, _)) => q"assert(true)"
          case util.Failure(ParseException(_, msg)) => q"""assert(false, "parse failure: " + $msg)"""
          case util.Success(_) => q"""assert(false, "code compiles")"""
          case util.Failure(err) => q"assert(false, error: ${err.toString})"
        }
      case _ => c.abort(c.enclosingPosition, "Supplied Literal must be a string")
    }
  }

}
