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

  def testGridNumberFilled() = {
    val s = List(
      List(false, true),
      List(true, false)
    )
    val g = new Grid(s)
    // testing number of filled cells in solution
    assertEquals(2, g.numberFilled())
    assertEquals(2, g.numberFilled(s.flatten))


    val myg = new UserGrid(g)
    // start: must be empty
    assertEquals(0, myg.numberFilled())
    myg.change(0, 0, Filled())
    myg.change(1, 1, Filled())
    // 2 filled
    assertEquals(2, myg.numberFilled())
  }

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

  def testGridByRowCol() = {
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

  def testUserGridUserSolution() = {
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
      assertTrue(checkType(y, None()))
    }

    // test affectation
    ug.change(0, 0, MaybeEmpty())
    ug.change(0, 1, MaybeFilled())
    ug.change(1, 0, Empty())
    ug.change(1, 1, Filled())
    assertTrue(checkType(ug.userSolution(0)(0), MaybeEmpty()))
    assertTrue(checkType(ug.userSolution(0)(1), MaybeFilled()))
    assertTrue(checkType(ug.userSolution(1)(0), Empty()))
    assertTrue(checkType(ug.userSolution(1)(1), Filled()))

    // test reset
    ug.removeAllMaybe()
    // must be reset
    assertTrue(checkType(ug.userSolution(0)(0), None()))
    assertTrue(checkType(ug.userSolution(0)(1), None()))
    // must not have changed with reset
    assertFalse(checkType(ug.userSolution(1)(0), None()))
    assertFalse(checkType(ug.userSolution(1)(1), None()))

    // test maybe validation
    ug.change(0, 0, MaybeEmpty())
    ug.change(0, 1, MaybeFilled())
    ug.validateAllMaybe()
    assertTrue(checkType(ug.userSolution(0)(0), Empty()))
    assertTrue(checkType(ug.userSolution(0)(1), Filled()))

    // reset the game
    ug.resetGame()
    assertTrue(checkType(ug.userSolution(1)(0), None()))
    assertTrue(checkType(ug.userSolution(1)(1), None()))
  }

  def testUserGridCheckGameFinishedAgainstSolution() = {
    // grid from specified content
    val s = List(
      List(false, false, false),
      List(false, false, true),
      List(true, false, false)
    )
    val g = new Grid(s)
    val myGrid = new UserGrid(g)

    // empty grid: false
    assertFalse(myGrid.checkGameFinishedAgainstSolution())

    myGrid.change(2, 0, Filled())
    // partially correct: false
    assertFalse(myGrid.checkGameFinishedAgainstSolution())

    myGrid.change(1, 2, Filled())
    // filled correctly: true
    assertTrue(myGrid.checkGameFinishedAgainstSolution())

    myGrid.change(2, 2, MaybeFilled())
    // filled correctly + maybe state: false
    assertFalse(myGrid.checkGameFinishedAgainstSolution())

    myGrid.change(2, 2, Filled())
    // filled correctly + too many filled : false
    assertFalse(myGrid.checkGameFinishedAgainstSolution())

    myGrid.change(1, 2, Filled())
    // filled partially + other filled : false
    assertFalse(myGrid.checkGameFinishedAgainstSolution())

  }

  def testUserGridCheckGameFinishedAgainstHints() = {
    // grid from specified content
    val s = List(
      List(false, true),
      List(true, false)
    )
    val g = new Grid(s)
    val myGrid = new UserGrid(g)

    // empty solution is not correct
    assertFalse(myGrid.checkGameFinishedAgainstSolution())
    assertFalse(myGrid.checkGameFinishedAgainstHints())

    // correct against solution (and obviously against the hints, too)
    myGrid.change(0, 1, Filled())
    myGrid.change(1, 0, Filled())
    assertTrue(myGrid.checkGameFinishedAgainstSolution())
    assertTrue(myGrid.checkGameFinishedAgainstHints)

    // not correct against solution, but correct against hints
    myGrid.resetGame()
    myGrid.change(0, 0, Filled())
    myGrid.change(1, 1, Filled())
    assertFalse(myGrid.checkGameFinishedAgainstSolution())
    assertTrue(myGrid.checkGameFinishedAgainstHints)

    // not correct against hints (number correct, columns correct, rows incorrect)
    myGrid.resetGame()
    myGrid.change(0, 0, Filled())
    myGrid.change(0, 1, Filled())
    assertFalse(myGrid.checkGameFinishedAgainstHints)

    // not correct against hints (number correct, columns incorrect, rows correct)
    myGrid.resetGame()
    myGrid.change(0, 0, Filled())
    myGrid.change(1, 0, Filled())
    assertFalse(myGrid.checkGameFinishedAgainstHints)
  }

}
