package xieyuheng.bebop

trait JoinAble[E] {
  def join(left: E, right: E): E
}

object JoinAble {
  def newReplaceOld[E] = new JoinAble[E] {
    def join(left: E, right: E): E = right
  }
}
