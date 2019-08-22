package bebop.ein

trait PreOrder[E] {
  def pre(a: E, b: E): Boolean

  def strictPre(a: E, b: E): Boolean =
    pre(a, b) && a != b

  def preReflexive(a: E): Law =
    True(pre(a, a))

  def preTransitive(a: E, b: E, c: E): Law = {
    True(pre(a, b)) and
    True(pre(b, c)) implies
    True(pre(a, c))
  }
}
