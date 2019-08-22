package bebop.ein

object util {
  def id[A](a: A): A = a

  def const[A, B](a: A): B => A = {
    (_: B) => a
  }
}
