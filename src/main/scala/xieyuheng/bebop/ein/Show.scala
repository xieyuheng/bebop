package xieyuheng.bebop.ein

trait Show[A] {
  def format(value: A): String
}

object Show {
  implicit object stringShow extends Show[String] {
    def format(value: String): String = value
  }

  implicit object intShow extends Show[Int] {
    def format(value: Int): String = value.toString
  }

  implicit object doubleShow extends Show[Double] {
    def format(value: Double): String = value.toString
  }

  def apply[A](implicit printable: Show[A]): Show[A] =
    printable

  def format[A](value: A)(implicit printable: Show[A]): String =
    printable.format(value)

  def print[A](value: A)(implicit printable: Show[A]): Unit =
    scala.Predef.print(printable.format(value))

  def println[A](value: A)(implicit printable: Show[A]): Unit =
    scala.Predef.println(printable.format(value))
}

case class Cat(name: String, age: Double, color: String)

object Cat {
  implicit object printable extends Show[Cat] {
    def format(cat: Cat): String = {
      val name  = Show.format(cat.name)
      val age   = Show.format(cat.age)
      val color = Show.format(cat.color)
      s"$name is a $age year-old $color cat."
    }
  }
}

object ShowSyntax {
  implicit class ShowOps[A](value: A) {
    def format(implicit printable: Show[A]): String =
      printable.format(value)

    def print(implicit printable: Show[A]): Unit =
      scala.Predef.print(printable.format(value))

    def println(implicit printable: Show[A]): Unit =
      scala.Predef.println(printable.format(value))
  }
}

object ShowCatExample extends App {
  val catMom = Cat("臭狗屎", 2.5, "黄色条文")
  val catChild1 = Cat("赵铁柱", 1, "灰色条文")
  val catChild2 = Cat("赵德柱", 1, "灰色条文")

  Show.println(catMom)
  Show.println(catChild1)
  Show.println(catChild2)

  import ShowSyntax._

  catMom.println
  catChild1.println
  catChild2.println
}
