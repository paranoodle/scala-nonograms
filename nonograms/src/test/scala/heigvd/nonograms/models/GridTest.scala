package heigvd.nonograms.models

import junit.framework.Assert._
import junit.framework._

class GridTest extends TestCase {

  // ********** TESTS GRID ********** //

  /**
    * This tests the basic construction of a random grid with specified sizes.
    */
  def testGridRandomTestSize() = {
    // rectangular grid
    val x = 2
    val y = 3
    val grid1 = new Grid(x, y)
    assertEquals(x, grid1.sizeX)
    assertEquals(y, grid1.sizeY)

    // square grid
    val grid2 = new Grid(y)
    assertEquals(y, grid2.sizeX)
    assertEquals(y, grid2.sizeY)

    // default sized grid
    val grid3 = new Grid
    assertEquals(10, grid3.sizeX)
    assertEquals(10, grid3.sizeY)
  }

  /**
    * This tests the basic construction of a grid with specified content, with (non-)/ human readable content
    */
  def testGridFromContentTestReadability1(): Unit = {
    val preparedGrid = List(
      List(true, false, false),
      List(true, false, true),
      List(true, true, false),
      List(true, true, true),
      List(false, false, false),
      List(false, false, true),
      List(false, true, false),
      List(false, true, true)
    )
    val gridNonHumanReadable = new Grid(preparedGrid, false)
    val gridYesHumanReadable = new Grid(preparedGrid) // default should be human-readable

    assertEquals(preparedGrid(0).size, gridNonHumanReadable.sizeY)
    assertEquals(preparedGrid.size, gridNonHumanReadable.sizeX)

    assertEquals(preparedGrid(0).size, gridYesHumanReadable.solution.size)
    assertEquals(preparedGrid.size, gridYesHumanReadable.solution(0).size)

    assertEquals(preparedGrid(0).size, gridYesHumanReadable.sizeX)
    assertEquals(preparedGrid.size, gridYesHumanReadable.sizeY)

    assertEquals(true, gridNonHumanReadable.solution(0)(0))
    assertEquals(false, gridNonHumanReadable.solution(0)(1))
    assertEquals(false, gridNonHumanReadable.solution(0)(2))
    assertEquals(true, gridNonHumanReadable.solution(1)(0))
    assertEquals(false, gridNonHumanReadable.solution(1)(1))
    assertEquals(true, gridNonHumanReadable.solution(2)(0))

    assertEquals(true, gridYesHumanReadable.solution(0)(0))
    assertEquals(true, gridYesHumanReadable.solution(0)(1))
    assertEquals(true, gridYesHumanReadable.solution(0)(2))
    assertEquals(false, gridYesHumanReadable.solution(1)(0))
    assertEquals(false, gridYesHumanReadable.solution(1)(1))
    assertEquals(false, gridYesHumanReadable.solution(2)(0))
  }

  def testGridFromContentTestReadability2(): Unit = {
    val preparedGridB = List(
      List(false, true),
      List(false, true),
      List(true, false)
    )
    val grid3 = new Grid(preparedGridB, false)
    val grid4 = new Grid(preparedGridB) // default should be human-readable

    assertEquals(false, grid3.solution(0)(0))
    assertEquals(true, grid3.solution(0)(1))
    assertEquals(false, grid3.solution(1)(0))
    assertEquals(true, grid3.solution(1)(1))
    assertEquals(true, grid3.solution(2)(0))
    assertEquals(false, grid3.solution(2)(1))

    assertEquals(false, grid4.solution(0)(0))
    assertEquals(false, grid4.solution(0)(1))
    assertEquals(true, grid4.solution(1)(0))
    assertEquals(true, grid4.solution(1)(1))
    assertEquals(true, grid4.solution(0)(2))
    assertEquals(false, grid4.solution(1)(2))
  }

  /**
    * This tests the construction of the grid from a human-readable list of 0/1
    */
  def testGridFromStringList(): Unit = {
    val preparedGrid = List(
      "01",
      "01",
      "10"
    )
    val grid1 = new Grid(preparedGrid, 1) // default should be human-readable
    val grid2 = new Grid(preparedGrid, 2, false)

    assertEquals(false, grid1.solution(0)(0))
    assertEquals(false, grid1.solution(0)(1))
    assertEquals(true, grid1.solution(1)(0))
    assertEquals(true, grid1.solution(1)(1))
    assertEquals(true, grid1.solution(0)(2))
    assertEquals(false, grid1.solution(1)(2))

    assertEquals(false, grid2.solution(0)(0))
    assertEquals(true, grid2.solution(0)(1))
    assertEquals(false, grid2.solution(1)(0))
    assertEquals(true, grid2.solution(1)(1))
    assertEquals(true, grid2.solution(2)(0))
    assertEquals(false, grid2.solution(2)(1))
  }

  /**
    * This tests nothing. It's only print methods...
    */
  def testPrintsDoNotCrash(): Unit = {
    val grid = new Grid()
    grid.printGrid()
    grid.printHints()
    assert(true)
  }

  /**
    * This tests the construction of grids with specified rows / cols.
    */
  def testGridByRowCol() = {
    // grid from specified content
    val preparedGrid = List(
      List(false, false, false),
      List(false, false, true),
      List(false, true, false),
      List(false, true, true),
      List(true, false, false),
      List(true, false, true),
      List(true, true, false),
      List(true, true, true))
    val preparedCols = List(
      List(),
      List(1),
      List(1),
      List(2),
      List(1),
      List(1, 1),
      List(2),
      List(3))
    val preparedRows = List(
      List(4),
      List(2, 2),
      List(1, 1, 1, 1)
    )
    val grid = new Grid(preparedGrid, false)
    assertEquals(preparedRows, grid.rows_hint)
    assertEquals(preparedCols, grid.cols_hint)
    assertEquals(preparedGrid(0).size, grid.sizeY)
    assertEquals(preparedGrid.size, grid.sizeX)

    assertEquals(false, grid.solution(6)(2))
    assertEquals(true, grid.solution(4)(0))
  }

  /**
    * This tests the number of filled cells in both grid and related userGrid
    */
  def testGridNumberFilled() = {
    val preparedGrid = List(
      List(false, true),
      List(true, false)
    )
    val grid = new Grid(preparedGrid, false)
    // testing number of filled cells in solution
    assertEquals(2, grid.numberFilled())
    assertEquals(2, grid.numberFilled(preparedGrid.flatten))

    val userGrid = new UserGrid(grid)
    // start: must be empty
    assertEquals(0, userGrid.numberFilledCache)
    userGrid.change(0, 0, Filled())
    userGrid.change(1, 1, Filled())
    // 2 filled
    assertEquals(2, userGrid.numberFilledCache)
  }
}