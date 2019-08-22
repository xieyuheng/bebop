package bebop.ein

trait Functor[F[_]] {
  def map[A, B](c: F[A])(f: A => B): F[B]

  def fmap[A, B](f: A => B): (F[A] => F[B]) = (c) => map(c)(f)

  def replace[A, B](c: F[A])(b: B): F[B] = map(c)(util.const(b))

  def mapRespectId[A](c: F[A]) =
    Equal(map(c)(util.id), c)

  def mapRespectCompose[A, B, C](c: F[A])(f: A => B, g: B => C) =
    Equal(map(c)(f andThen g), map(map(c)(f))(g))
}

object Functor {
  def map[A, B, F[_]](c: F[A])(f: A => B)
    (implicit functor: Functor[F]): F[B] = {
    functor.map(c)(f)
  }
}
