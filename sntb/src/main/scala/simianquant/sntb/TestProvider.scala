package simianquant.sntb

import language.experimental.macros
import reflect.macros.blackbox.Context
import reflect.macros.{ParseException, TypecheckException}

trait TestProvider {

  private var runList: List[Block] = Nil

  private val className = this.getClass.getSimpleName()

  def assertCompiles(str: String): Unit = macro TestProvider.assertCompilesImpl

  def assertTypeError(str: String): Unit = macro TestProvider.assertTypeErrorImpl

  object it {
    def should(name: String): BlockBuilder = BlockBuilder(name)
  }

  def run: Unit = {
    println(s"$className: ")
    runList foreach {
      case Block(name, exec) =>
        print(s"- $name...")
        exec()
        println("done")
    }
  }

  case class BlockBuilder(name: String) {
    def in(exec: => Unit): Unit = runList = new Block(name, () => exec) :: runList
  }

  private case class Block(name: String, exec: Function0[Unit])

}

object TestProvider {

  def assertCompilesImpl(c: Context)(str: c.Tree): c.Tree = {
    import c.universe._
    str match {
      case q"${ y: String }" =>
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
      case q"${ y: String }" =>
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
