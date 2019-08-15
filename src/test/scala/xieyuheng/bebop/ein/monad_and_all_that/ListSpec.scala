import org.scalatest._
import xieyuheng.bebop.ein.monad_and_all_that._

class ListSpec extends FlatSpec with Matchers {
  val list1: List[String] = Cons("a", Cons("b", Cons("c", Nil())))
  val list2: List[Int] = Cons(1, Cons(2, Cons(3, Nil())))

  "List" can "be mapped over" in {
    assert(List.listFunctor.map(list1)(_.toUpperCase) ==
      Cons("A", Cons("B", Cons("C", Nil()))))

    import xieyuheng.bebop.ein.Functor._

    assert(map(list1)(_.toUpperCase) ==
      Cons("A", Cons("B", Cons("C", Nil()))))

    assert(list1.map(_.toUpperCase) ==
      Cons("A", Cons("B", Cons("C", Nil()))))
  }

  it can "be replaced" in {
    assert(List.listFunctor.replace(list1)(0) ==
      Cons(0, Cons(0, Cons(0, Nil()))))
  }

  it can "append" in {
    assert(
      List.append(
        Cons(1, Cons(2, Cons(3, Nil()))),
        Cons(4, Cons(5, Cons(6, Nil())))) ==
    Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Cons(6, Nil())))))))
  }

  it can "comprehension" in {
    val list1 = Cons("A", Cons("B", Cons("C", Cons("D", Nil()))))
    val list2 = Cons("1", Cons("2", Cons("3", Nil())))
    val resultList = for {
      x <- list1
      y <- list2
    } yield s"${x}.${y}"

    assert(resultList ==
      Cons("A.1",Cons("A.2",Cons("A.3",
        Cons("B.1",Cons("B.2",Cons("B.3",
          Cons("C.1",Cons("C.2",Cons("C.3",
            Cons("D.1",Cons("D.2",Cons("D.3",Nil())))))))))))))
  }
}
