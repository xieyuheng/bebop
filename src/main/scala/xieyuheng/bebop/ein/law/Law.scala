package xieyuheng.bebop.ein

sealed trait Law {
  def check: Boolean

  def and(that: Law) = And(this, that)
  def or(that: Law) = Or(this, that)
  def implies(that: Law) = Implication(this, that)
}

final case class Equal[A](
  lhs: A,
  rhs: A,
  equal: (A, A) => Boolean =
    (x: A, y: A) => x == y,
) extends Law {
  def check = equal(lhs, rhs)
}

final case class True(bool: Boolean) extends Law {
  def check = bool == true
}

final case class False(bool: Boolean) extends Law {
  def check = bool == false
}

final case class And(
  left: Law,
  right: Law,
) extends Law {
  def check = left.check && right.check
}

final case class Or(
  left: Law,
  right: Law,
) extends Law {
  def check = left.check || right.check
}

final case class Implication(
  condition: Law,
  conclusion: Law,
) extends Law {
  def check =
    if (condition.check) {
      conclusion.check
    } else {
      true
    }
}
