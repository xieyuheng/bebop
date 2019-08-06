package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

trait Tran1[A1, R]
    // extends Cell[Tran1[A1, R]]
{
  implicit val arg1Lattice: JoinSemilattice[A1]
  implicit val retLattice: JoinSemilattice[R]
  implicit val system: ActorSystem

  def connect
    (arg1Cell: Cell[A1],
      retCell: Cell[R]): Unit

  def apply
    (arg1Cell: Cell[A1],
      cellName: String = ""): Cell[R] = {
    val retCell = ValueCell[R](cellName)
    connect(arg1Cell, retCell)
    retCell
  }

  /** extends Cell */

  // implicit val lattice: JoinSemilattice[Tran1[A1, R]] = ???

  def foreach(f: Option[Tran1[A1, R]] => Unit): Unit = ???

  def put(a: Tran1[A1, R]): Unit = ???

  def asArgOf(propagator: Propagator, n: Int): Unit = ???

  def forward(cell: Cell[Tran1[A1, R]]): Unit = ???
}
