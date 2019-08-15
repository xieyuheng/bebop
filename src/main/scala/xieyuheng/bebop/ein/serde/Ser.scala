package xieyuheng.bebop.ein.serde

/** Serialize to Obj */
trait Ser[A] {
  def toObj(value: A): Obj
}

object Ser {
  def apply[A](implicit ser: Ser[A]): Ser[A] =
    ser

  def toObj[A](value: A)(implicit ser: Ser[A]): Obj =
    ser.toObj(value)

  implicit object stringSer extends Ser[String] {
    def toObj(str: String): Obj =
      StrObj(str)
  }
}

case class Person(name: String, email: String)

object Person {
  implicit object ser extends Ser[Person] {
    def toObj(person: Person): Obj =
      MapObj(Map(
        "name" -> StrObj(person.name),
        "email" -> StrObj(person.email)))
  }
}

object SerdeExample {
  implicit class SerOps[A](value: A) {
    def toObj(implicit ser: Ser[A]): Obj =
      ser.toObj(value)
  }

  def main(args: Array[String]): Unit = {
    /** General interface */
    println(Ser[Person].toObj(Person("Dave", "dave@example.com")))
    // == with explicit argument ==>
    println(Ser[Person](Person.ser).toObj(Person("Dave", "dave@example.com")))

    /** Special interface */
    println(Ser.toObj(Person("Dave", "dave@example.com")))
    // == with explicit argument ==>
    println(Ser.toObj(Person("Dave", "dave@example.com"))(Person.ser))

    /** Type enrichment */
    println(Person("Dave", "dave@example.com").toObj)
    // == with explicit class ==>
    println(SerOps(Person("Dave", "dave@example.com")).toObj)
    // == with explicit argument ==>
    println(SerOps(Person("Dave", "dave@example.com")).toObj(Person.ser))
  }
}
