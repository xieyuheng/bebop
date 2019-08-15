package xieyuheng.bebop.ein

trait PartialOrder[E] extends PreOrder[E] {
  def antisymmetric(a: E, b: E): Law = {
    True(pre(a, b)) and
    True(pre(b, a)) implies
    True(a == b)
  }
}
