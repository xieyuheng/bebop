import org.scalatest._
import bebop.ein.monad_and_all_that._

class TreeSpec extends FlatSpec with Matchers {
  val tree1: Tree[String] =
    Branch(
      Leaf("a"),
      Branch(
        Leaf("b"),
        Leaf("c")))

  val tree2: Tree[Int] =
    Branch(
      Leaf(1),
      Branch(
        Leaf(2),
        Leaf(3)))

  "Tree" can "be mapped over" in {
    import bebop.ein.Functor._

    assert(map(tree1)(_.toUpperCase) ==
      Branch(Leaf("A"),Branch(Leaf("B"),Leaf("C"))))

    assert(map(tree2)(_ * 2) ==
      Branch(Leaf(2),Branch(Leaf(4),Leaf(6))))

    assert(tree1.map(_.toUpperCase) ==
      Branch(Leaf("A"),Branch(Leaf("B"),Leaf("C"))))

    assert(tree2.map(_ * 2) ==
      Branch(Leaf(2),Branch(Leaf(4),Leaf(6))))
  }

  it can "also be zipped" in {
    assert(Tree.zip(tree1, tree2) ==
      Just(Branch(
        Leaf (("a", 1)),
        Branch(
          Leaf(("b", 2)),
          Leaf(("c", 3))))))
  }

  it can "depthLabel" in {
    assert(Tree.depthLabel(tree1).run(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))
    assert(Tree.depthLabel(tree1).run(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))
    assert(Tree.depthLabel(tree1).run(100) == (103,Branch(Leaf(100),Branch(Leaf(101),Leaf(102)))))

    import bebop.ein.monad_and_all_that.depthLabel._

    assert(NonFunctional.depthLabel(tree1) == Branch(Leaf(0),Branch(Leaf(1),Leaf(2))))
    assert(NonFunctional.depthLabel(tree1) == Branch(Leaf(3),Branch(Leaf(4),Leaf(5))))

    assert(NaiveFunctional.depthLabel(tree1)(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))
    assert(NaiveFunctional.depthLabel(tree1)(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))

    assert(ProFunctional.depthLabel(tree1)(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))
    assert(ProFunctional.depthLabel(tree1)(0) == (3,Branch(Leaf(0),Branch(Leaf(1),Leaf(2)))))
  }
}
