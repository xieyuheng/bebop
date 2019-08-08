package xieyuheng.bebop

trait JoinAble[E] {
  def join(left: E, right: E): E
}
