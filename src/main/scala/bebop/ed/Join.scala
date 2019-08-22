package bebop.ed

trait Join[E] {
  def join(left: E, right: E): E
}

object Join {
  def newReplaceOld[E] = new Join[E] {
    def join(left: E, right: E): E = right
  }
}
