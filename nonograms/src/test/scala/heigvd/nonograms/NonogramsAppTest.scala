package heigvd.nonograms

import junit.framework._
import Assert._

object NonogramsAppTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[NonogramsAppTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

/**
  * Unit test for simple App.
  */
class NonogramsAppTest extends TestCase("app") {

  def testRandomGridSize() = {
    // rectangular grid
    val x = 2
    val y = 3
    val g = new Grid(x, y)
    assertEquals(x, g.sizeX)
    assertEquals(y, g.sizeY)

    // square grid
    val h = new Grid(y)
    assertEquals(y, h.sizeX)
    assertEquals(y, h.sizeY)

    // default sized grid
    val i = new Grid
    assertEquals(10, i.sizeX)
    assertEquals(8, i.sizeY)
  }

  def testMyGridRowCol() = {
    // grid from specified content
    val s = List(
      List(false, false, false),
      List(false, false, true),
      List(false, true, false),
      List(false, true, true),
      List(true, false, false),
      List(true, false, true),
      List(true, true, false),
      List(true, true, true))
    val cols = List(
      List(),
      List(1),
      List(1),
      List(2),
      List(1),
      List(1, 1),
      List(2),
      List(3))
    val rows = List(
      List(4),
      List(2, 2),
      List(1, 1, 1, 1)
    )
    val g = new Grid(s)
    assertEquals(rows, g.rows_hint)
    assertEquals(cols, g.cols_hint)
    assertEquals(s(0).size, g.sizeY)
    assertEquals(s.size, g.sizeX)

    assertEquals(false, g.solution(6)(2))
    assertEquals(true, g.solution(4)(0))
  }

  def testMyGridMySolution() = {
    val g = new Grid()
    val ug = new UserGrid(g)

    // test size
    assertEquals(g.sizeX, ug.userSolution.size)
    assertEquals(g.sizeY, ug.userSolution(0).size)

    // helper to check type
    def checkType(value: CellType, typ: CellType): Boolean = value match {
      case `typ` => true
      case _ => false
    }

    // test all none at begining
    for (x <- ug.userSolution; y <- x) {
      assert(checkType(y, None()))
    }

    // test affectation
    ug.change(0, 0, MaybeEmpty())
    ug.change(0, 1, MaybeFilled())
    ug.change(1, 0, Empty())
    ug.change(1, 1, Filled())
    assert(checkType(ug.userSolution(0)(0), MaybeEmpty()))
    assert(checkType(ug.userSolution(0)(1), MaybeFilled()))
    assert(checkType(ug.userSolution(1)(0), Empty()))
    assert(checkType(ug.userSolution(1)(1), Filled()))

    // test reset
    ug.removeAllMaybe()
    // must be reset
    assert(checkType(ug.userSolution(0)(0), None()))
    assert(checkType(ug.userSolution(0)(1), None()))
    // must not have changed with reset
    assertFalse(checkType(ug.userSolution(1)(0), None()))
    assertFalse(checkType(ug.userSolution(1)(1), None()))

    // test maybe validation
    ug.change(0, 0, MaybeEmpty())
    ug.change(0, 1, MaybeFilled())
    ug.validateAllMaybe()
    assert(checkType(ug.userSolution(0)(0), Empty()))
    assert(checkType(ug.userSolution(0)(1), Filled()))

    // reset the game
    ug.resetGame()
    assert(checkType(ug.userSolution(1)(0), None()))
    assert(checkType(ug.userSolution(1)(1), None()))
  }

}
