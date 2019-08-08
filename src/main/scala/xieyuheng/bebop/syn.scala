package xieyuheng.bebop

object syn {
  def ifo[A]
    (control: Cell[Boolean],
      consequent: => Cell[A],
      alternate: => Cell[A],
      output: Cell[A]): Unit = {

  }
}
