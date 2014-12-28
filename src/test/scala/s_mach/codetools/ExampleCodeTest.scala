package s_mach.codetools

import org.scalatest.{Matchers, FlatSpec}

object ExampleCodeTest {

  import s_mach.codetools.reflectPrint._

  case class Movie(
    name: String,
    year: Int
  )

  object Movie {
    implicit val reflectPrint_Movie = ReflectPrint.forProductType[Movie]
  }

  case class Name(
    firstName: String,
    middleName: Option[String],
    lastName: String
  )

  object Name {
    implicit val reflectPrint_Name = ReflectPrint.forProductType[Name]
  }


  case class Actor(
    name: Name,
    age: Int,
    movies: Set[Movie]
  )

  object Actor {
    implicit val reflectPrint_Person = ReflectPrint.forProductType[Actor]
  }

  val n1 = Name("Gary",Some("Freakn"),"Oldman")
  val n2 = Name("Guy",None,"Pearce")
  val n3 = Name("Lance",None,"Gatlin")

  val m1 = Movie("The Professional",1994)
  val m2 = Movie("The Fifth Element",1997)
  val m3 = Movie("Memento",1994)
  val m4 = Movie("Prometheus",2000)

  val a1 = Actor(n1,56,Set(m1,m2))
  val a2 = Actor(n2,47,Set(m3,m4))
  val a3 = Actor(n3,37,Set.empty)
}

class ExampleCodeTest extends FlatSpec with Matchers {
  import ExampleCodeTest._
  import s_mach.codetools.reflectPrint._

  "ReflectPrint.printApply" must "generate code that can recreate the same value" in {
    a1.printApply should equal(
      """Actor(name=Name(firstName="Gary",middleName=Some("Freakn"),lastName="Oldman"),age=56,movies=Set(Movie(name="The Professional",year=1994),Movie(name="The Fifth Element",year=1997)))"""
    )
    val alt1 = Actor(name=Name(firstName="Gary",middleName=Some("Freakn"),lastName="Oldman"),age=56,movies=Set(Movie(name="The Professional",year=1994),Movie(name="The Fifth Element",year=1997)))
    alt1 should equal(a1)
  }

  "ReflectPrint.printUnapply" must "generate code that can recreate the same value as unapply" in {
    a1.printUnapply should equal(
      """(Name(firstName="Gary",middleName=Some("Freakn"),lastName="Oldman"),56,Set(Movie(name="The Professional",year=1994),Movie(name="The Fifth Element",year=1997)))"""
    )
    val ualt1 = (Name(firstName="Gary",middleName=Some("Freakn"),lastName="Oldman"),56,Set(Movie(name="The Professional",year=1994),Movie(name="The Fifth Element",year=1997)))
    ualt1 should equal(Actor.unapply(a1).get)
  }

  "ReflectPrint.printApply" must "generate verbose format with ReflectPrintFormat.Implicits.verbose in scope" in {
    import ReflectPrintFormat.Implicits.verbose

    a2.printApply should equal { """
Actor(
  name = Name(
    firstName = "Guy",
    middleName = None,
    lastName = "Pearce"
  ),
  age = 47,
  movies = Set(
    Movie(
      name = "Memento",
      year = 1994
    ),
    Movie(
      name = "Prometheus",
      year = 2000
    )
  )
)
""".trim
    }

    a3.printApply should equal { """
Actor(
  name = Name(
    firstName = "Lance",
    middleName = None,
    lastName = "Gatlin"
  ),
  age = 37,
  movies = Set.empty
)
""".trim
    }
  }
}
