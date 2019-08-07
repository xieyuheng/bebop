package xieyuheng.bebop

import xieyuheng.pracat.JoinSemilattice

import akka.actor.ActorSystem

class Tran1Cell[A1, R]
  (implicit system: ActorSystem)
    extends ValueCell[Tran1[A1, R]](None)(Tran1Cell.lattice, system) {


}

object Tran1Cell {
  def lattice[A1, R]: JoinSemilattice[Tran1[A1, R]] = ???
}
