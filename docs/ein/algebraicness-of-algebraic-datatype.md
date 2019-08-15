# Algebraic-ness of Algebraic Datatype

The pattern we are using is called "algebraic datatype", why such datatypes are called "algebraic" ?
Thinking of number of elements in the type, `Boolean` has two, `Color` might has three -- `Red`, `Green` and `Blue`,
thus product of type `Boolean` and `Color` such as `Tuple2[Boolean, Color]` has six elements,
sum of type `Boolean` and `Color` such as `Either[Boolean, Color]` has five elements.
(i.e. interpreting type as set, and thinking of cardinal number of set.)

Also product type can be interpreted as logic conjunction (logic "and"),
sum type can be interpretered as logic disjunction (logic "or").
That is to say, to construct an element of type `Tuple2[Boolean, Color]`,
we must construct an element of `Boolean` **and** an element of `Color`;
while, to construct an element of type `Either[Boolean, Color]`,
we must construct an element of `Boolean` **or** an element of `Color`.

| Type theory   | Set theory                          | Cardinal number       | Logic                         |
|---------------|-------------------------------------|-----------------------|-------------------------------|
| Product type  | Cartesian product                   | Product of number     | Conjunction (and)             |
| Sum type      | Disjoint union                      | Sum of number         | Disjunction (or)              |
| Function type | Exponential of set (function space) | Exponential of number | Implication (if ... then ...) |
| Unit type     | Set of one element (singleton set)  | 1                     | Truth                         |
| Bottom type   | Empty set                           | 0                     | Falsehood                     |

- Bottom type is called `Nothing` in Scala.
- The above table is part of [Curry–Howard correspondence](https://en.wikipedia.org/wiki/Curry–Howard_correspondence)

Since we have algebraic interpretation, we can have algebraic equations.
For examples, the follow types have same information:
``` scala
Unit * A  ≌  (Unit, A)  ≌  A
B + C => A  ≌  (B => A) * (C => A)  ≌  ((B => A), (C => A))
Boolean => A  ≌  A^2  ≌  A * A  ≌  (A, A)
B * C => A  ≌  A^(B*C)  ≌  (A^C)^B  ≌  B => C => A
```

(The last example is called [currying](https://en.wikipedia.org/wiki/Currying).)

We can also solve equations:
``` scala
List[A] == Nil + Cons(A, List[A]) == 1 + A * List[A]
List[A] - A * List[A] == 1
(1 - A) * List[A] == 1
List[A] == 1 / (1 - A)
List[A] == 1 + A + A*A + A*A*A + A*A*A*A + ...
```

Yes! `List[A]` is infinite sum of bigger and bigger tuples.

Yes! recursive datatypes are solutions to algebraic equations.
