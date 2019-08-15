# Algebraic Datatype in Other Languages

In Haskell:
``` haskell
data Tree a = Leaf a | Branch (Tree a) (Tree a)
```

In Typescript, by abstract class:
``` typescript
abstract class Tree<A> {}

class Leaf<A> extends Tree<A> {
  constructor (public value: A) { super() }
}

class Branch<A> extends Tree<A> {
  constructor (public left: A, public right: A) { super() }
}
```

In Typescript, by interface:
``` typescript
interface Tree<A> {}

class Leaf<A> implements Tree<A> {
  constructor (public value: A) {}
}

class Branch<A> implements Tree<A> {
  constructor (public left: A, public right: A) {}
}
```
