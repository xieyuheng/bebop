package xieyuheng.bebop.ein.monad_and_all_that

import xieyuheng.bebop.ein._

sealed trait List[A]
final case class Nil[A]() extends List[A]
final case class Cons[A](head: A, tail: List[A]) extends List[A]

object List {
  implicit val listFunctor = new Functor[List] {
    def map[A, B](list: List[A])(f: A => B): List[B] = {
      list match {
        case Nil() => Nil()
        case Cons(head, tail) => Cons(f(head), map(tail)(f))
      }
    }
  }

  def append[A](x: List[A], y: List[A]): List[A] = {
    x match {
      case Nil() => y
      case Cons(head, tail) => Cons(head, append(tail, y))
    }
  }

  implicit val listMonad = new Monad[List] {
    def unit[A](value: A): List[A] = Cons(value, Nil())

    def flatMap[A, B](list: List[A])(f: A => List[B]): List[B] = {
      list match {
        case Nil() => Nil()
        case Cons(head, tail) => append(f(head), flatMap(tail)(f))
      }
    }
  }

  implicit class FunctorExtension[A](list: List[A]) {
    def map[B](f: A => B): List[B] = listFunctor.map(list)(f)
  }

  implicit class MonadExtension[A](list: List[A]) {
    def flatMap[B](f: A => List[B]): List[B] =
      listMonad.flatMap(list)(f)
  }
}
