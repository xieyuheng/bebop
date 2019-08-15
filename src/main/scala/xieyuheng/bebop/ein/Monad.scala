package xieyuheng.bebop.ein

trait Monad[M[_]] extends Functor[M] {
  def unit[A](value: A): M[A]

  def flatMap[A, B](c: M[A])(f: A => M[B]): M[B]

  def map[A, B](c: M[A])(f: A => B): M[B] =
    flatMap(c)(a => unit(f(a)))
}
