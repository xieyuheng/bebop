package xieyuheng.bebop.ein.monad_and_all_that.depthLabel

import xieyuheng.bebop.ein.monad_and_all_that._

object NonFunctional {
  var counter = 0

  def tick = {
    val result = counter
    counter += 1
    result
  }

  def depthLabel[A](tree: Tree[A]): Tree[Int] = {
    tree match {
      case Leaf(value) => Leaf(tick)
      case Branch(left, right) => Branch(depthLabel(left), depthLabel(right))
    }
  }
}

object NaiveFunctional {
  def depthLabel[A](tree: Tree[A])(counter: Int): (Int, Tree[Int]) = {
    tree match {
      case Leaf(value) => (counter + 1, Leaf(counter))
      case Branch(left, right) => {
        val (counter2, leftLable) = depthLabel(left)(counter)
        val (counter3, rightLable) = depthLabel(right)(counter2)
        (counter3, Branch(leftLable, rightLable))
      }
    }
  }
}

object ProFunctional {
  def unit[A, S](value: A): S => (S, A) =
    counter => (counter, value)

  def flatMap[A, B, S]
    (state: S => (S, A))
    (f: A => S => (S, B)): S => (S, B) = { s => {
      val (s2, a) = state(s)
      f(a)(s2)
    }
  }

  def tick(x: Int) = (x + 1, x)

  def depthLabel[A](tree: Tree[A]): Int => (Int, Tree[Int]) = {
    tree match {
      case Leaf(value) =>
        flatMap(tick) { counter => unit(Leaf(counter)) }
      case Branch(left, right) => {
        flatMap(depthLabel(left)) { leftLable =>
          flatMap(depthLabel(right)) { rightLable =>
            unit(Branch(leftLable, rightLable))
          }
        }
      }
    }
  }
}
