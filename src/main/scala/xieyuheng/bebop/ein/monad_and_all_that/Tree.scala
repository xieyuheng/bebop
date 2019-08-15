package xieyuheng.bebop.ein.monad_and_all_that

import xieyuheng.bebop.ein._

sealed trait Tree[A]
final case class Leaf[A](value: A) extends Tree[A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  implicit val treeFunctor = new Functor[Tree] {
    def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = {
      tree match {
        case Leaf(value) => Leaf(f(value))
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
      }
    }
  }

  implicit class FunctorExtension[A](tree: Tree[A]) {
    def map[B](f: A => B): Tree[B] = treeFunctor.map(tree)(f)
  }

  def zip[A, B](x: Tree[A], y: Tree[B]): Maybe[Tree[(A, B)]] = {
    (x, y) match {
      case (Leaf(value1), Leaf(value2)) => Just(Leaf(value1, value2))
      case (Branch(left1, right1), Branch(left2, right2)) => for {
        zippedLeft <- zip(left1, left2)
        zippedRight <- zip(right1, right2)
      } yield Branch(zippedLeft, zippedRight)
      case _ => Nothing()
    }
  }

  def tick: State[Int, Int] = State { x => (x + 1, x) }

  def depthLabel[A](tree: Tree[A]): State[Int, Tree[Int]] = {
    tree match {
      case Leaf(value) => for {
        counter <- tick
      } yield Leaf(counter)
      case Branch(left, right) => for {
        leftLable <- depthLabel(left)
        rightLable <- depthLabel(right)
      } yield Branch(leftLable, rightLable)
    }
  }
}
