# Monad And All That

"Monad And All That" by John Hughes
- [Origin Lecture Home Page](https://www.cs.uoregon.edu/research/summerschool/summer12/curriculum.html)
  - [Lecture 1 -- Monads](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that.pdf)
    -- [Exercises](http://www.cse.chalmers.se/~rjmh/OPLSS/Exercises.pdf)
  - [Lecture 2 -- Monad Transformers](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that%20--%20II.pdf)
  - [Lecture 3 -- Applicative Functors](http://www.cse.chalmers.se/~rjmh/OPLSS/Monads%20and%20all%20that%20--%20III.%20Applicative%20Functors.pdf)
- Humbly modified for Scala by Xie Yuheng

## Contains

- [Algebraic Datatype](#algebraic-datatype)
- [Structural Recursion](#structural-recursion)
- [Typeclass](#typeclass)
- [Type Enrichment](#type-enrichment)
- [Monad](#monad)
- [Monad Transformers](#monad-transformers)
- [Applicative Functors](#applicative-functors)

## Algebraic Datatype

Problems:
- How to model data?

### Binary Tree (with `maybeMonad`)

We will use `Tree` as example datatype in the following
(All of our examples will be written in Scala):

``` scala
sealed trait Tree[A]
final case class Leaf[A](value: A) extends Tree[A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

val tree1: Tree[String] =
  Branch(
    Leaf("a"),
    Branch(
      Leaf("b"),
      Leaf("c")))

val tree2: Tree[Int] =
  Branch(
    Leaf(1),
    Branch(
      Leaf(2),
      Leaf(3)))
```

### List

Scala has [`scala.collection.immutable.List`](https://www.scala-lang.org/api/current/scala/collection/immutable/List.html),
let us define our own:

``` scala
sealed trait List[A]
final case class Nil[A]() extends List[A]
final case class Cons[A](head: A, tail: List[A]) extends List[A]

val list1: List[String] =
  Cons("a", Cons("b", Cons("c", Nil())))

val list2: List[Int] =
  Cons(1, Cons(2, Cons(3, Nil())))
```

- Extra credits:
  - [algebraic-datatype-in-other-languages](algebraic-datatype-in-other-languages.md)
  - [algebraicness-of-algebraic-datatype](algebraicness-of-algebraic-datatype.md)

## Structural Recursion

> The code pretty much writes itself. -- Bartosz Milewski

Problems:
- How to process data?

### Mapping over List and Tree

``` scala
def listMap[A, B](list: List[A])(f: A => B): List[B] = {
  list match {
    case Nil() => Nil()
    case Cons(head, tail) => Cons(f(head), listMap(tail)(f))
  }
}

listMap(list1)(_.toUpperCase)
// => Cons("A", Cons("B", Cons("C", Nil())))

def treeMap[A, B](tree: Tree[A])(f: A => B): Tree[B] = {
  tree match {
    case Leaf(value) => Leaf(f(value))
    case Branch(left, right) => Branch(treeMap(left)(f), treeMap(right)(f))
  }
}

treeMap(tree1)(_.toUpperCase)
// Branch(Leaf("A"),Branch(Leaf("B"),Leaf("C")))
```

## Typeclass

Problems:
- How to achieve polymorphism by typeclass?

### Functor

A `treeMap` or `listMap` like function `def map[A, B](c: F[A])(f: A => B): F[B]`,
can often be implemented for datatypes.
We can abstract this function to a typeclass called `Functor`:
``` scala
trait Functor[F[_]] {
  def map[A, B](c: F[A])(f: A => B): F[B]
}
```

We defined our first typeclass!

Now we implement the typeclass for `List` and `Tree`:
``` scala
val listFunctor = new Functor[List] {
  def map[A, B](list: List[A])(f: A => B): List[B] = {
    list match {
      case Nil() => Nil()
      case Cons(head, tail) => Cons(f(head), map(tail)(f))
    }
  }
}

val treeFunctor = new Functor[Tree] {
  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = {
    tree match {
      case Leaf(value) => Leaf(f(value))
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
    }
  }
}
```

We can call the `map` function in the two instances of `Functor` directly:
``` scala
listFunctor.map(list1)(_.toUpperCase)
treeFunctor.map(tree1)(_.toUpperCase)
```

We can also abstract over the instance,
by passing instance as argument,
and define a generic interface function `map`
for the typeclass `Functor`:
``` scala
def map[A, B, F[_]](c: F[A])(f: A => B)(functor: Functor[F]): F[B] = {
  functor.map(c)(f)
}

map(list1)(_.toUpperCase)(listFunctor)
map(tree1)(_.toUpperCase)(treeFunctor)
```

We can make the instance argument **implicit**:
``` scala
def map[A, B, F[_]](c: F[A])(f: A => B)(implicit functor: Functor[F]): F[B] = {
  functor.map(c)(f)
}
```

And declare our definition of `listFunctor` and `treeFunctor` to be **implicit** too:
``` scala
implicit val listFunctor = new Functor[List] {
  def map[A, B](list: List[A])(f: A => B): List[B] = {
    list match {
      case Nil() => Nil()
      case Cons(head, tail) => Cons(f(head), map(tail)(f))
    }
  }
}

implicit val treeFunctor = new Functor[Tree] {
  def map[A, B](tree: Tree[A])(f: A => B): Tree[B] = {
    tree match {
      case Leaf(value) => Leaf(f(value))
      case Branch(left, right) => Branch(map(left)(f), map(right)(f))
    }
  }
}
```

By doing so, we can call the generic `map` function
without the implicit argument,
(the compiler will search for implicit arguments by type in current scope):
``` scala
map(list1)(_.toUpperCase)
map(tree1)(_.toUpperCase)
```

This is how we can achieve functional style polymorphism in Scala,
- i.e. by using typeclass and implicit arguments.

### Organizing instances of typeclass

An instance of a typeclass are often placed
in the companion object of the argument type of the typeclass.
(where the compiler will also searched for implicit arguments)

Adding `treeFunctor` to the companion object of `Tree`:
``` scala
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
}
```

Adding `listFunctor` to the companion object of `List`:
``` scala
sealed trait List[A]
final case class Nil[A]() extends List[A]
final case class Cons[A](head: A, tail: List[A]) extends List[A]

object List {
  implicit val listFunctor = new Functor[List] {
    def map[A, B](lis: List[A])(f: A => B): List[B] = {
      lis match {
        case Nil() => Nil()
        case Cons(head, tail) => Cons(f(head), map(tail)(f))
      }
    }
  }
}
```

### Organizing interface functions of typeclass

An interface function of a typeclass,
are often placed in the companion object of the typeclass.

Adding `map` interface function to the companion object of `Functor`:
``` scala
trait Functor[F[_]] {
  def map[A, B](c: F[A])(f: A => B): F[B]
}

object Functor {
  def map[A, B, F[_]](c: F[A])(f: A => B)
    (implicit functor: Functor[F]): F[B] = {
    functor.map(c)(f)
  }
}
```

## Type Enrichment

Problems:
- How to implement object oriented API?

Instead of `map(tree)(f)`,
we would like to use API like `tree.map(f)`,
as if `.map` is a method of the type `Tree`.

We can achieve this by a functional design pattern called **type enrichment** or **type extension**.

- [Essential Scala: Enriched Interfaces](https://books.underscore.io/essential-scala/essential-scala.html#enriched-interfaces)

Adding implicit class to the companion object of `Tree`:
``` scala
object Tree {
  // ...

  implicit class FunctorExtension[A](tree: Tree[A]) {
    def map[B](f: A => B): Tree[B] = functor.map(tree)(f)
  }
}
```

Adding implicit class to the companion object of `List`:
``` scala
object List {
  // ...

  implicit class FunctorExtension[A](list: List[A]) {
    def map[B](f: A => B): List[B] = functor.map(list)(f)
  }
}
```

By doing so,
we can call `.map` on `Tree` and `List`,
as if `.map` is a method of these types.
``` scala
list1.map(_.toUpperCase)
tree1.map(_.toUpperCase)
```

We achieved this without class inheritance and class hierarchy. Awesome!

## Monad

Problems:
- What is Monad?
- Why Monad is important for (functional) programmers?

### Zipping Trees

For example:
``` scala
Branch(
  Leaf("a"),
  Branch(
    Leaf("b"),
    Leaf("c")))
// and
Branch(
  Leaf(1),
  Branch(
    Leaf(2),
    Leaf(3)))
// ==>
Branch(
  Leaf ((1, "a")),
  Branch(
    Leaf((2, "b")),
    Leaf((3, "c"))))
```

Let us implement it:
``` scala
def zipTree[A, B](x: Tree[A], y: Tree[B]): Tree[(A, B)] = {
  (x, y) match {
    case (Leaf(value1), Leaf(value2)) => Leaf(value1, value2)
    case (Branch(left1, right1), Branch(left2, right2)) =>
      Branch(zipTree(left1, left2), zipTree(right1, right2))
    case _ => ???
  }
}
```

What should we do in the above `???`

Instead of throwing exception,
we model exception by datatype in functional programming.

Scala has `Option` type, but let us define our own version:
``` scala
sealed trait Maybe[A]
final case class Nothing[A]() extends Maybe[A]
final case class Just[A](value: A) extends Maybe[A]
```

Let us try to implement `zipTree` again:
``` scala
def zipTree[A, B](x: Tree[A], y: Tree[B]): Maybe[Tree[(A, B)]] = {
  (x, y) match {
    case (Leaf(value1), Leaf(value2)) => Just(Leaf((value1, value2)))
    case (Branch(left1, right1), Branch(left2, right2)) =>
      zipTree(left1, left2) match {
        case Nothing() => Nothing()
        case Just(zippedLeft) => zipTree(right1, right2) match {
          case Nothing() => Nothing()
          case Just(zippedRight) => Just(Branch(zippedLeft, zippedRight))
        }
      }
    case _ => Nothing()
  }
}
```

It seems something terrible happened! The nested matches!

As a programmer, we abstract.
Let us abstract the common pattern of the use of `Maybe`, to `unit` and `flatMap`:
``` scala
def unit[A](value: A): Maybe[A] = Just(value)

def flatMap[A, B](c: Maybe[A])(f: A => Maybe[B]): Maybe[B] = {
  c match {
    case Nothing() => Nothing()
    case Just(value) => f(value)
  }
}

def zipTree[A, B](x: Tree[A], y: Tree[B]): Maybe[Tree[(A, B)]] = {
  (x, y) match {
    case (Leaf(value1), Leaf(value2)) => unit(Leaf((value1, value2)))
    case (Branch(left1, right1), Branch(left2, right2)) =>
      flatMap(zipTree(left1, left2)) { zippedLeft =>
        flatMap(zipTree(right1, right2)) { zippedRight =>
          unit(Branch(zippedLeft, zippedRight))
        }
      }
    case _ => Nothing()
  }
}
```

We can use type enrichment to attach `.flatMap` method on `Tree`:
``` scala
def zipTree[A, B](x: Tree[A], y: Tree[B]): Maybe[Tree[(A, B)]] = {
  (x, y) match {
    case (Leaf(value1), Leaf(value2)) => Just(Leaf((value1, value2)))
    case (Branch(left1, right1), Branch(left2, right2)) =>
      zipTree(left1, left2).flatMap { zippedLeft =>
        zipTree(right1, right2).map { zippedRight =>
          Branch(zippedLeft, zippedRight)
        }
      }
    case _ => Nothing()
  }
}
```

It feels better already,
it seems that `flatMap` is **binding** a value
of type `T` in `Maybe[T]`, to the callback function.
- This is why `flatMap` is called `bind` in Haskell.

But can we do better? How about this:
``` scala
def zipTree[A, B](x: Tree[A], y: Tree[B]): Maybe[Tree[(A, B)]] = {
  (x, y) match {
    case (Leaf(value1), Leaf(value2)) => Just(Leaf((value1, value2)))
    case (Branch(left1, right1), Branch(left2, right2)) => for {
      zippedLeft <- zipTree(left1, left2)
      zippedRight <- zipTree(right1, right2)
    } yield Branch(zippedLeft, zippedRight)
    case _ => Nothing()
  }
}
```

The `for { ... } yield <Exp>` is a syntax sugar, provided by Scala.

- [Scala FAQ: How does `yield` work?](https://docs.scala-lang.org/tutorials/FAQ/yield.html)

``` scala
for {
  zippedLeft <- zipTree(left1, left2)
  zippedRight <- zipTree(right1, right2)
} yield Branch(zippedLeft, zippedRight)

// translated into:
zipTree(left1, left2).flatMap { zippedLeft =>
  zipTree(right1, right2).map { zippedRight =>
    Branch(zippedLeft, zippedRight)
  }
}

// in general

for {
  x <- c1
  y <- c2
  z <- c3
} yield <Exp>

// translated into:
c1.flatMap { x =>
c2.flatMap { y =>
c3.map { z => <Exp>}}}
```

We can use `for { ... } yield <Exp>` as long as we define method `.flatMap` and `.map` for the type.

Let abstract `unit` and `flatMap` to a typeclass called `Monad`:
``` scala
trait Monad[M[_]] {
  def unit[A](value: A): M[A]

  def flatMap[A, B](c: M[A])(f: A => M[B]): M[B]
}
```

The above instance of `Monad` is very famous,
it is called `maybeMonad`,
which is often used to solve the null exception problem.

Adding `maybeMonad` to the companion object of `Maybe`:
``` scala
object Maybe {
  // ...

  implicit val maybeMonad = new Monad[Maybe] {
    def unit[A](value: A): Maybe[A] = Just(value)

    def flatMap[A, B](maybe: Maybe[A])(f: A => Maybe[B]): Maybe[B] = {
      maybe match {
        case Nothing() => Nothing()
        case Just(value) => f(value)
      }
    }
  }

  /* Also adding the `.flatMap` method to `Maybe` */
  implicit class MonadExtension[A](maybe: Maybe[A]) {
    def flatMap[B](f: A => Maybe[B]): Maybe[B] =
      monad.flatMap(maybe)(f)
  }
}
```

Actually, if we can implement `unit` and `flatMap`, we can also implement `map`,
thus an instance of `Monad` is also an instance of `Functor`:
``` scala
trait Monad[M[_]] extends Functor[M] {
  def unit[A](value: A): M[A]

  def flatMap[A, B](c: M[A])(f: A => M[B]): M[B]

  def map[A, B](c: M[A])(f: A => B): M[B] =
    flatMap(c)(a => unit(f(a)))
}
```

### List Comprehension (with `listMonad`)

Since we can implement meaningful `unit` and `flatMap` for `List`,
`List` is also a `Monad`:
- And this is why `Monad`'s main method is called `flatMap` in Scala.
``` scala
object List {
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

  implicit class MonadExtension[A](list: List[A]) {
    def flatMap[B](f: A => List[B]): List[B] =
      listMonad.flatMap(list)(f)
  }
}
```

List Comprehension:
``` scala
val list1 = Cons("A", Cons("B", Cons("C", Cons("D", Nil()))))
val list2 = Cons("1", Cons("2", Cons("3", Nil())))
for {
  x <- list1
  y <- list2
} yield s"${x}.${y}"

// Cons("A.1",Cons("A.2",Cons("A.3",
//   Cons("B.1",Cons("B.2",Cons("B.3",
//     Cons("C.1",Cons("C.2",Cons("C.3",
//       Cons("D.1",Cons("D.2",Cons("D.3",Nil()))))))))))))
```

### Label Leaves with Depth-First-Order Index (with `stateMonad`)

For example:
``` scala
Branch(
  Leaf("a"),
  Branch(
    Leaf("b"),
    Leaf("c")))
// ==>
Branch(
  Leaf(1),
  Branch(
    Leaf(2),
    Leaf(3)))
```

NonFunctional:
``` scala
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

/* BEWARE OF THE SIDE-EFFECT */

depthLabel(tree1)
// Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))
depthLabel(tree1)
// Branch(Leaf(3),Branch(Leaf(4),Leaf(5)))
```

NaiveFunctional:
``` scala
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

/* NO SIDE-EFFECT */

depthLabel(tree1)(0)
// (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2))))
depthLabel(tree1)(0)
// (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2))))
depthLabel(tree1)(100)
// (103,Branch(Leaf(100),Branch(Leaf(101),Leaf(102))))
```

ProFunctional:
``` scala
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

depthLabel(tree1)(0)
// (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2))))
depthLabel(tree1)(100)
// (103,Branch(Leaf(100),Branch(Leaf(101),Leaf(102))))
```

`State` and `stateMonad`:
``` scala
case class State[S, A](run: S => (S, A))

object State {
  implicit def stateMonad[S] = {
    new Monad[State[S, ?]] {
      def unit[A](a: A): State[S, A] =
        State { s => (s, a) }

      def flatMap[A, B]
        (state: State[S, A])
        (f: A => State[S, B]): State[S, B] = State { s => {
          val (s2, a) = state.run(s)
          f(a).run(s2)
        }
      }
    }
  }

  implicit class FunctorExtension[A, S](state: State[S, A]) {
    def map[B](f: A => B): State[S, B] = stateMonad.map(state)(f)
  }

  implicit class MonadExtension[A, S](state: State[S, A]) {
    def flatMap[B](f: A => State[S, B]) =
      stateMonad.flatMap(state)(f)
  }
}
```

`depthLabel` with `stateMonad`:
``` scala
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

depthLabel(tree1).run(0)
// (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2))))
depthLabel(tree1).run(100)
// (103,Branch(Leaf(100),Branch(Leaf(101),Leaf(102))))
```

### Logging (with `loggerMonad`)

Our `loggerMonad` is a simplified `writerMonad`:
``` scala
case class Logger[A](value: A, log: Vector[String])

object Logger {
  implicit def loggerMonad = {
    new Monad[Logger] {
      def unit[A](a: A): Logger[A] =
        Logger(a, Vector())

      def flatMap[A, B]
        (logger: Logger[A])
        (f: A => Logger[B]): Logger[B] = {
        val logger2 = f(logger.value)
        Logger(logger2.value, logger.log ++ logger2.log)
      }
    }
  }

  implicit class FunctorExtension[A](logger: Logger[A]) {
    def map[B](f: A => B): Logger[B] = loggerMonad.map(logger)(f)
  }

  implicit class MonadExtension[A](logger: Logger[A]) {
    def flatMap[B](f: A => Logger[B]) =
      loggerMonad.flatMap(logger)(f)
  }
}
```

Using `loggerMonad` to trace function:
``` scala
def gcd(x: Int, y: Int): Int = {
  if (y == 0) x else gcd(y, x % y)
}

def logger[A](x: A, message: String): Logger[A] =
  Logger(x, Vector(message))

def info(message: String): Logger[Int] =
  Logger(0, Vector(message))

def gcdLogger(x: Int, y: Int): Logger[Int] = {
  if (y == 0) logger(x, s"Result: ${x}")
  else for {
    _ <- info(s"${x} mod ${y} == ${x % y}")
    result <- gcdLogger(y, x % y)
  } yield result
}

gcdLogger(1071, 462).log.foreach(println)
// 1071 mod 462 == 147
// 462 mod 147 == 21
// 147 mod 21 == 0
// Result: 21
```

## [TODO] Monad Transformers

## [TODO] Applicative Functors
