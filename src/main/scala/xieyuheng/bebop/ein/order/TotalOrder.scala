package xieyuheng.bebop.ein

trait TotalOrder[E] extends PartialOrder[E] {
  def connex(a: E, b: E): Law =
    True(pre(a, b)) or True(pre(b, a))
}
