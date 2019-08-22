package bebop.ein.monad_and_all_that

import bebop.ein._

sealed trait Maybe[A]
final case class Nothing[A]() extends Maybe[A]
final case class Just[A](value: A) extends Maybe[A]

object Maybe {
  implicit val maybeFunctor = new Functor[Maybe] {
    def map[A, B](maybe: Maybe[A])(f: A => B): Maybe[B] = {
      maybe match {
        case Nothing() => Nothing()
        case Just(value) => Just(f(value))
      }
    }
  }

  implicit class FunctorExtension[A](maybe: Maybe[A]) {
    def map[B](f: A => B): Maybe[B] =
      maybeFunctor.map(maybe)(f)
  }

  implicit val maybeMonad = new Monad[Maybe] {
    def unit[A](value: A): Maybe[A] = Just(value)

    def flatMap[A, B](maybe: Maybe[A])(f: A => Maybe[B]): Maybe[B] = {
      maybe match {
        case Nothing() => Nothing()
        case Just(value) => f(value)
      }
    }
  }

  implicit class MonadExtension[A](maybe: Maybe[A]) {
    def flatMap[B](f: A => Maybe[B]): Maybe[B] =
      maybeMonad.flatMap(maybe)(f)
  }
}
