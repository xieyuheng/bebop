// package xieyuheng.propagator.sequential

// case class Propagator() {
//   def attach(cells: List[Cell]) = {}

//   def $ (cell: Cell) = attach(cells)
// }

// case class Content() {}

// case class Cell(name: String) {
//   def add(content: Content) = 1

//   def <= (content: Content) = add(content)

//   def register = {}

//   def content = {}
// }

// object CellApp extends App {
//   Cell("asd") <= Content()
// }

// object PropagatorApp extends App {
//   val propagator = Propagator()

//   propagator
//     .from(Cell :: Cell :: HNil)
//     .to(Cell)

//   val cell = propagator(Cell, Cell)
// }

// case class Network() {

// }
