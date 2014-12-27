package s_mach.codetools

import scala.language.higherKinds
import scala.collection.generic.CanBuildFrom

sealed trait IssueLevel
object IssueLevel {
  case object Error extends IssueLevel
  case object Warning extends IssueLevel
}
case class Issue(level: IssueLevel, message: String)

sealed trait Result[+A] {
  def isSuccess : Boolean
  def isFailure : Boolean
  def get : A
  def zomIssue: List[Issue]
  def map[B](f: A => B) : Result[B]
  def flatMap[B](f: A => Result[B]) : Result[B]
  def toOption: Option[A]
  def prepend(_zomIssue: List[Issue]) : Result[A]
  def fold[X, AA >: A](isSuccess: Result.Success[AA] => X, isFailure: Result.Failure => X) : X
}

object Result {
  object Error {
    def apply(message: String) : Issue = Issue(IssueLevel.Error, message)
    def unapply(issue: Issue) : Option[String] = issue.level match {
      case IssueLevel.Error => Some(issue.message)
      case _ => None
    }
  }
  object Warning {
    def apply(message: String) : Issue = Issue(IssueLevel.Warning, message)
    def unapply(issue: Issue) : Option[String] = issue.level match {
      case IssueLevel.Warning => Some(issue.message)
      case _ => None
    }
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

  def applicative[A,B,ZZ](ra: Result[A], rb: Result[B])(f: (A,B) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue
    if(ra.isSuccess && rb.isSuccess) {
      f(ra.get,rb.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }
  def applicative[A,B,C,ZZ](ra: Result[A], rb: Result[B],rc: Result[C])(f: (A,B,C) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue ::: rc.zomIssue
    if(ra.isSuccess && rb.isSuccess && rc.isSuccess) {
      f(ra.get,rb.get,rc.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }
  def applicative[A,B,C,D,ZZ](ra: Result[A], rb: Result[B],rc: Result[C],rd: Result[D])(f: (A,B,C,D) => Result[ZZ]) : Result[ZZ] = {
    val zomIssue = ra.zomIssue ::: rb.zomIssue ::: rc.zomIssue ::: rd.zomIssue
    if(ra.isSuccess && rb.isSuccess && rc.isSuccess) {
      f(ra.get,rb.get,rc.get,rd.get).prepend(zomIssue)
    } else {
      Failure(zomIssue)
    }
  }

  case class Failure(zomIssue: List[Issue]) extends Result[Nothing] {

    override def isSuccess = false

    override def get = throw new NoSuchElementException

    override def prepend(_zomIssue: List[Issue]) = copy(zomIssue = _zomIssue ::: zomIssue)

    override def isFailure = true

    override def toOption = None

    override def map[B](f: (Nothing) => B): Result[B] = this

    override def flatMap[B](f: Nothing => Result[B]): Result[B] = this

    override def fold[X, AA >: Nothing](isSuccess: Success[AA] => X, isFailure: Failure => X): X = isFailure(this)
  }

  case class Success[+A](value: A, zomIssue: List[Issue]) extends Result[A] {

    override def isSuccess = true

    override def get: A = value

    override def prepend(_zomIssue: List[Issue]) = copy(zomIssue = _zomIssue ::: zomIssue)

    override def isFailure = false

    override def toOption = Some(value)

    override def map[B](f: (A) => B): Result[B] = {
      Success(f(value),zomIssue)
    }

    override def flatMap[B](f: A => Result[B]): Result[B] = {
      f(value).prepend(zomIssue)
    }

    override def fold[X, AA >: A](isSuccess: Success[AA] => X, isFailure: Failure => X): X = isSuccess(this)
  }

  def error[A](msg: String) : Result[A] = Failure(Error(msg) :: Nil)
  def errors[A](msg: String) : Result[A] = Failure(Error(msg) :: Nil)
  def apply[A](value: A, warning: String*) : Success[A] = Success(value, warning.map(Warning.apply).toList)
  def unapply[A](r: Result[A]) : Option[(Option[A],List[Issue])] = Some{
    r match {
      case Success(value, oomIssue) => (Some(value), oomIssue)
      case Failure(oomIssue) => (None, oomIssue)
    }
  }

}
