package bebop.peirce

class FormalContext[Obj, Attr]
  (val objects: Set[Obj],
    val attributes: Set[Attr],
    val relation: Set[(Obj, Attr)]) {

  def extentOf(intent: Set[Attr]) = {
    var extent = Set[Obj]()
    for {
      attr <- intent
      obj <- objects
    } {
      if (relation.contains(obj, attr)) {
        extent += obj
      }
    }
    extent
  }

  def intentOf(extent: Set[Obj]) = {
    var intent = Set[Attr]()
    for {
      attr <- attributes
      obj <- extent
    } {
      if (relation.contains(obj, attr)) {
        intent += attr
      }
    }
    intent
  }

  def extentClosure(extent: Set[Obj]) = extentOf(intentOf(extent))

  def intentClosure(intent: Set[Attr]) = intentOf(extentOf(intent))
}
