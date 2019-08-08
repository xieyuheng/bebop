package xieyuheng.bebop

trait joinAble[E] {
  def join(left: E, right: E): E
}
