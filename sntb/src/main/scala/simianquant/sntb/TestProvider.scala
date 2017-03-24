package simianquant.sntb

import language.experimental.macros
import reflect.macros.blackbox.Context
import reflect.macros.{ParseException, TypecheckException}

trait TestProvider {

  def assertCompiles(str: String): Unit = macro TestProvider.assertCompilesImpl

  def assertTypeError(str: String): Unit = macro TestProvider.assertTypeErrorImpl

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
