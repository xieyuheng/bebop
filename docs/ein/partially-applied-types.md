# Partially Applied Types

https://stackoverflow.com/questions/45271911/functor-instance-for-type-constructor-with-two-parameters-in-scala

https://github.com/typelevel/kind-projector

``` scala
// kind-projector CompilerPlugin:
State[S, ?]
Lambda[A => State[S, A]]
// equal to:
({ type SS[A] = State[S, A] })#SS
```
