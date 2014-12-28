/*
                    ,i::,
               :;;;;;;;
              ;:,,::;.
            1ft1;::;1tL
              t1;::;1,
               :;::;               _____       __  ___              __
          fCLff ;:: tfLLC         / ___/      /  |/  /____ _ _____ / /_
         CLft11 :,, i1tffLi       \__ \ ____ / /|_/ // __ `// ___// __ \
         1t1i   .;;   .1tf       ___/ //___// /  / // /_/ // /__ / / / /
       CLt1i    :,:    .1tfL.   /____/     /_/  /_/ \__,_/ \___//_/ /_/
       Lft1,:;:       , 1tfL:
       ;it1i ,,,:::;;;::1tti      s_mach.codetools
         .t1i .,::;;; ;1tt        Copyright (c) 2014 S-Mach, Inc.
         Lft11ii;::;ii1tfL:       Author: lance.gatlin@gmail.com
          .L1 1tt1ttt,,Li
            ...1LLLL...
*/
package s_mach.codetools

import scala.language.higherKinds
import scala.collection.generic.CanBuildFrom

/**
 * A monad for the result of some computation that may fail with multiple errors
 * or complete successfully with informative or warning messages. The order 
 * that issues (i.e. errors, warnings and info) accumulate is preserved.
 *
 * @tparam A type of the computation
 */
sealed trait Result[+A] {
  import Result._

  /** @return TRUE if the result is successful */
  def isSuccess : Boolean
  /** @return TRUE if the result is a failure */
  def isFailure : Boolean
  /**
   * @throws NoSuchElementException if the result is a failure
   * @return the value of the computation */
  def get : A
  /** @return zero or more issues raised during the computation */
  def zomIssue: List[Issue]
  /** @return If Result is successful, the value is applied to f */
  def foreach[U](f: A => U) : Unit
  /** @return If Result is successful, a new Result with the value returned from
    *         applying the value to f. If failure, no change */
  def map[B](f: A => B) : Result[B]
  /** @return If Result is successful, a new Result with the value returned from
    *         applying the value to f . If failure, no change */
  def flatMap[B](f: A => Result[B]) : Result[B]
  /** @return If successful, Some(value) otherwise None */
  def toOption: Option[A]
  /** @return a new Result with the supplied list of issues prepended to the
    *         list of issues*/
  def prepend(_zomIssue: List[Issue]) : Result[A]
  /** @return If successful, the result of applying this to isSuccess. If
    *         failure, the result fo applying this to isFailure */
  def fold[X, AA >: A](isSuccess: Success[AA] => X, isFailure: Failure => X) : X
}

object Result {
  /** @return a success result with possible warnings */
  def apply[A](value: A, warning: String*) : Success[A] =
    Success(value, warning.map(Warning.apply).toList)
  /** @return a failing result with an error */
  def error[A](msg: String) : Result[A] = Failure(Error(msg) :: Nil)
  /** @return a failing result with errors */
  def errors[A](msg: String) : Result[A] = Failure(Error(msg) :: Nil)
  def unapply[A](r: Result[A]) : Option[(Option[A],List[Issue])] = Some{
    r match {
      case Success(value, oomIssue) => (Some(value), oomIssue)
      case Failure(oomIssue) => (None, oomIssue)
    }
  }

  case class Failure(zomIssue: List[Issue]) extends Result[Nothing] {

    override def isSuccess = false

    override def get = throw new NoSuchElementException

    override def prepend(_zomIssue: List[Issue]) =
      copy(zomIssue = _zomIssue ::: zomIssue)

    override def isFailure = true

    override def toOption = None

    override def foreach[U](f: (Nothing) => U) = { }

    override def map[B](f: (Nothing) => B): Result[B] = this

    override def flatMap[B](f: Nothing => Result[B]): Result[B] = this

    override def fold[X, AA >: Nothing](
      isSuccess: Success[AA] => X,
      isFailure: Failure => X
    ): X = isFailure(this)
  }

  case class Success[+A](value: A, zomIssue: List[Issue]) extends Result[A] {

    override def isSuccess = true

    override def get: A = value

    override def prepend(_zomIssue: List[Issue]) =
      copy(zomIssue = _zomIssue ::: zomIssue)

    override def isFailure = false

    override def toOption = Some(value)

    override def foreach[U](f: A => U) = f(value)

    override def map[B](f: (A) => B): Result[B] = Success(f(value),zomIssue)

    override def flatMap[B](f: A => Result[B]): Result[B] =
      f(value).prepend(zomIssue)

    override def fold[X, AA >: A](
      isSuccess: Success[AA] => X,
      isFailure: Failure => X
    ): X = isSuccess(this)
  }

  sealed trait Issue {
    def message: String
    def isError: Boolean
  }
  case class Error(message: String) extends Issue {
    override def isError = true
  }
  case class Warning(message: String) extends Issue {
    override def isError = false
  }
  case class Info(message: String) extends Issue {
    override def isError = false
  }

  def sequence[A,M[AA] <: Traversable[AA]](
    m: M[Result[A]]
  )(implicit
    cbf:CanBuildFrom[Nothing,A,M[A]]
  ) : Result[M[A]] = {
    val issueBuilder = List.newBuilder[Issue]
    m.foreach(r => issueBuilder ++= r.zomIssue)
    val zomIssue = issueBuilder.result()
    if(m.forall(_.isSuccess)) {
      val builder = cbf()
      m.foreach(r => builder += r.get)
      Success(builder.result(),zomIssue)
    } else {
      Failure(zomIssue)
    }
  }

  def applicative[A,B,ZZ](
    ra: Result[A],
    rb: Result[B]
  )(f: (A,B) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue
    if(ra.isSuccess && rb.isSuccess) {
      f(ra.get,rb.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }
  def applicative[A,B,C,ZZ](
    ra: Result[A],
    rb: Result[B],
    rc: Result[C]
  )(f: (A,B,C) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue ::: rc.zomIssue
    if(ra.isSuccess && rb.isSuccess && rc.isSuccess) {
      f(ra.get,rb.get,rc.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }
  def applicative[A,B,C,D,ZZ](
    ra: Result[A],
    rb: Result[B],
    rc: Result[C],
    rd: Result[D]
  )(f: (A,B,C,D) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue ::: rc.zomIssue ::: rd.zomIssue
    if(ra.isSuccess && rb.isSuccess && rc.isSuccess) {
      f(ra.get,rb.get,rc.get,rd.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }



}
