package heigvd.nonograms.models

import heigvd.nonograms.models
import junit.framework.Assert._
import junit.framework._

class UserGridTest extends TestCase {

  // ********** TESTS USER GRID ********** //

  /**
    * Tests penalties
    */
  def testPenalties {
    val s = List(
      List(true, false),
      List(false, false)
    )
    val grid = new Grid(s)
    val userGrid = new UserGrid(grid)
    userGrid.checkMode = true

    // 0 penalties at start up
    assertEquals(0, userGrid.numberPenaltiesCache)
    assertEquals(0, userGrid.penaltiesTime)

    // does en error in strict mode, penalties should have changed
    userGrid.leftClick(1,1)
    assertEquals(1, userGrid.numberPenaltiesCache)
    assert(userGrid.penaltiesTime > 0)
  }

  /**
    * Test if two longs are equals with a given delta precision.
    */
  private def isEqual(expected: Long, value: Long, delta : Long) : Unit = {
    assert(value > expected - delta)
    assert(value < expected + delta)
  }

  /**
    * Tests all the timings in user grid.
    */
  def testTimes (): Unit = {
    val s = List(
      List(true, false),
      List(false, false)
    )
    val grid = new Grid(s)
    val userGrid = new UserGrid(grid)
    // tolerance of 10ms...
    val delta = 10
    val timeToSleep = delta * 10

    def start = userGrid.time_start()
    def elapsed = userGrid.time_elapsed
    def finished = userGrid.time_finished()

    // TEST start: start is current, elapsed is 0
    val myStart = System.currentTimeMillis()
    isEqual(myStart, start, delta)
    isEqual(0, elapsed, delta)
    isEqual(0, finished, delta)

    // TEST game is going on: values updates
    Thread.sleep(timeToSleep)

    isEqual(myStart, start, delta)
    isEqual(timeToSleep, elapsed, delta)
    isEqual(timeToSleep, finished, delta)

    // TEST game finished : values freeze to end time
    Thread.sleep(timeToSleep)
    userGrid.change(0,0,Filled())

    val myend = System.currentTimeMillis() - myStart
    isEqual(myStart, start, delta)
    isEqual(myend, elapsed, delta)
    isEqual(myend, finished, delta)

    // TEST after game is finished for a long time yet: values keeps their values
    Thread.sleep(timeToSleep)
    isEqual(myStart, start, delta)
    isEqual(myend + timeToSleep, elapsed, delta)
    isEqual(myend, finished, delta)

    // TEST game reset : values reset.
    userGrid.resetGame()
    isEqual(System.currentTimeMillis(), start, delta)
    isEqual(0, elapsed, delta)
    isEqual(0, finished, delta)
  }

  // helper to check type
  private def checkType(value: CellType, typ: CellType): Boolean = value match {
    case `typ` => true
    case _ => false
  }

  /**
    * This tests the basics of user solution: change state, reset maybe, reset.
    */
  def testUserGridgetUserSolution() = {
    val preparedGrid = List(
      List(true, true, true, true, true, true),
      List(false, false, false, false, false, false)
    )
    val grid = new Grid(preparedGrid, true)
    val userGrid = new UserGrid(grid)

    // TEST INITIALIZATION : all cells have None() state at the beginning
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      assertTrue(checkType(userGrid.getUserSolution(x)(y), models.None()))
    }

    // TEST CHANGE: BASIC AFFECTATION IN NON-STRICT MODE
    // test of all types affectation, on both true and false real cell states
    // TEST: All affectations MUST be successful
    userGrid.checkMode = false

    userGrid.change(1, 0, MaybeEmpty())
    userGrid.change(2, 0, MaybeFilled())
    userGrid.change(3, 0, Empty())
    userGrid.change(4, 0, Filled())

    assertTrue(checkType(userGrid.getUserSolution(1)(0), MaybeEmpty()))
    assertTrue(checkType(userGrid.getUserSolution(2)(0), MaybeFilled()))
    assertTrue(checkType(userGrid.getUserSolution(3)(0), Empty()))
    assertTrue(checkType(userGrid.getUserSolution(4)(0), Filled()))

    userGrid.change(1, 1, MaybeEmpty())
    userGrid.change(2, 1, MaybeFilled())
    userGrid.change(3, 1, Empty())
    userGrid.change(4, 1, Filled())

    assertTrue(checkType(userGrid.getUserSolution(1)(1), MaybeEmpty()))
    assertTrue(checkType(userGrid.getUserSolution(2)(1), MaybeFilled()))
    assertTrue(checkType(userGrid.getUserSolution(3)(1), Empty()))
    assertTrue(checkType(userGrid.getUserSolution(4)(1), Filled()))

    // TEST: it should be impossible to affect Tried state.
    userGrid.change(5, 0, Tried())
    userGrid.change(5, 1, Tried())
    assertTrue(checkType(userGrid.getUserSolution(5)(1), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(5)(0), models.None()))

    // TEST: the untouched None() state is inaffected by all the changes
    assertTrue(checkType(userGrid.getUserSolution(0)(0), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(0)(1), models.None()))

    // TEST REMOVE MAYBE'S
    userGrid.removeAllMaybe()

    // Test: All "Maybe*" states must be reset to None()
    assertTrue(checkType(userGrid.getUserSolution(1)(0), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(2)(0), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(1)(1), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(2)(1), models.None()))

    // Test: All other states than "Maybe*" must not have changed with reset
    assertTrue(checkType(userGrid.getUserSolution(0)(0), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(3)(0), Empty()))
    assertTrue(checkType(userGrid.getUserSolution(4)(0), Filled()))
    assertTrue(checkType(userGrid.getUserSolution(5)(0), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(0)(1), models.None()))
    assertTrue(checkType(userGrid.getUserSolution(3)(1), Empty()))
    assertTrue(checkType(userGrid.getUserSolution(4)(1), Filled()))
    assertTrue(checkType(userGrid.getUserSolution(5)(1), models.None()))

    // TEST RESET: assert that ALL cells have None state.
    userGrid.resetGame()
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      assertTrue(checkType(userGrid.getUserSolution(x)(y), models.None()))
    }
  }

  /**
    * This tests the validation of "Maybe*" cells, in two configuration: strict and non-strict mode.
    */
  def testUserGridUserSolutionValidatesMaybe() = {
    // grid from specified content
    val preparedGrid = List(
      List(true, true, true),
      List(false, false, false)
      // to test MaybeEmpty() // to test MaybeFilled() // left to None()
    )
    val grid = new Grid(preparedGrid, true)

    // TWO user grids: Strict & Non-Strict.
    val userGridYesStrict = new UserGrid(grid)
    userGridYesStrict.checkMode = true
    val userGridNonStrict = new UserGrid(grid)
    userGridNonStrict.checkMode = false

    // test maybe validation
    userGridYesStrict.change(0, 0, MaybeEmpty())
    userGridYesStrict.change(0, 1, MaybeEmpty())
    userGridYesStrict.change(1, 0, MaybeFilled())
    userGridYesStrict.change(1, 1, MaybeFilled())

    userGridNonStrict.change(0, 0, MaybeEmpty())
    userGridNonStrict.change(0, 1, MaybeEmpty())
    userGridNonStrict.change(1, 0, MaybeFilled())
    userGridNonStrict.change(1, 1, MaybeFilled())

    userGridYesStrict.validateAllMaybe()
    userGridNonStrict.validateAllMaybe()

    // TEST: the untouched None() state is inaffected by all the changes
    assertTrue(checkType(userGridNonStrict.getUserSolution(2)(0), models.None()))
    assertTrue(checkType(userGridNonStrict.getUserSolution(2)(1), models.None()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(2)(0), models.None()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(2)(1), models.None()))

    // TEST: MaybeEmpty => Empty always, whatever the real value, whatever the strict mode
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(0), Empty()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(1), Empty()))
    assertTrue(checkType(userGridNonStrict.getUserSolution(0)(0), Empty()))
    assertTrue(checkType(userGridNonStrict.getUserSolution(0)(1), Empty()))

    // TEST: MaybeFilled => Filled if real value true, => Tried if real value false (in strict mode)
    assertTrue(checkType(userGridYesStrict.getUserSolution(1)(0), Filled()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(1)(1), Tried()))
    // TEST: MaybeFilled => Filled always if non-strict mode
    assertTrue(checkType(userGridNonStrict.getUserSolution(1)(0), Filled()))
    assertTrue(checkType(userGridNonStrict.getUserSolution(1)(1), Filled()))
  }

  /**
    * This tests the behavior of all possible clicks (right/left) on all possible states (5), with or without strict mode.
    */
  def testUserGridUserClicks() = {
    // grid from specified content
    val preparedGrid = List(
      List(true, true, true, true, true, true),
      List(true, true, true, true, true, true),
      List(false, false, false, false, false, false),
      List(false, false, false, false, false, false)
    )
    val grid = new Grid(preparedGrid, true)

    // TWO user grids: Strict & Non-Strict.
    val userGridYesStrict = new UserGrid(grid)
    userGridYesStrict.checkMode = true
    val userGridNonStrict = new UserGrid(grid)
    userGridNonStrict.checkMode = false

    for (y <- 0 until grid.sizeY) {
      userGridYesStrict.change(1, y, MaybeEmpty())
      userGridYesStrict.change(2, y, MaybeFilled())
      userGridYesStrict.change(3, y, Empty())
      userGridYesStrict.change(4, y, Filled())
    }

    // TEST: left click on None() state.
    userGridYesStrict.leftClick(0, 0, false)
    userGridYesStrict.leftClick(0, 1, true)
    userGridYesStrict.leftClick(0, 2, false)
    userGridYesStrict.leftClick(0, 3, true)
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(0), Filled()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(1), MaybeFilled()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(2), Tried()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(0)(3), MaybeFilled()))

    // To be continued with real tests of behavior, one for each case....
    // test two clicks (right and left) on all possible states (6), in two modes (strict or not), with or without draft activated.

    // tests ALL to click all the cells to go through all the code lines.
    userGridYesStrict.leftClick(0, 0) // default is false
    userGridYesStrict.rightClick(0, 0)
    for(x <- 0 until grid.sizeX - 1) {
      for(y <- 0 until grid.sizeY) {
        userGridYesStrict.leftClick(x, y, (y % 2 == 0))
        userGridYesStrict.rightClick(x, y, (y % 2 == 0))
      }
    }

    // TEST: the untouched None() state is unaffected by all the changes
    assertTrue(checkType(userGridYesStrict.getUserSolution(5)(0), models.None()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(5)(1), models.None()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(5)(2), models.None()))
    assertTrue(checkType(userGridYesStrict.getUserSolution(5)(3), models.None()))
  }

  /**
    * This tests the behavior of checking if the game is finished against the original solution.
    */
  def testUserGridCheckGameFinishedAgainstSolution() = {
    // grid from specified content
    val preparedGrid = List(
      List(false, true),
      List(true, false)
    )
    val grid = new Grid(preparedGrid, true)
    val userGrid = new UserGrid(grid)
    userGrid.checkMode = true //against the solution

    // empty grid: false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(1, 0, Filled())
    // partially correct: false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(1, 1, MaybeFilled())
    userGrid.change(0, 1, Filled())
    // filled correctly + maybe state: false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(1, 1, MaybeEmpty())
    // filled correctly + maybe state: false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(0, 1, models.None())
    userGrid.change(1, 1, Filled())
    // filled partially + other filled : false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(1, 1, Filled())
    userGrid.change(0, 1, Filled())
    // filled correctly + too many filled : false
    assertFalse(userGrid.isFinishedCache)

    userGrid.change(1, 1, Empty())
    // filled correctly: true
    assertTrue(userGrid.isFinishedCache)

  }

  /**
    * This tests nothing. It's only print methods...
    */
  def testPrintsDoNotCrash (): Unit = {
    val grid = new Grid()
    val userGrid = new UserGrid(grid)
    userGrid.generateRandomState()
    userGrid.printMyGrid()
    assert(true)
  }

  /**
    * This tests the behavior of checking if the game is finished against the hints (rows/columns)
    *
    * The following grid has two valid solutions against the hints, but a single solution is the "real solution" that was intended.
    * _1_1_
    * 1 | | |
    * 1 | | |
    *
    * Intended solution (from the original grid)
    * _1_1_
    * 1 |-|X|
    * 1 |X|-|
    *
    * Alternative solution (valid against the hints)
    * _1_1_
    * 1 |X|-|
    * 1 |-|X|
    */
  def testUserGridCheckGameFinishedAgainstHints() = {
    // grid from specified content
    val s = List(
      List(false, true),
      List(true, false)
    )
    val grid = new Grid(s, true)
    val userGridYesStrict = new UserGrid(grid)
    userGridYesStrict.checkMode = true // against solution
    val userGridNonStrict = new UserGrid(grid)
    userGridNonStrict.checkMode = false // against hints

    // TEST: empty solution is not correct
    assertFalse(userGridYesStrict.isFinishedCache)
    assertFalse(userGridNonStrict.isFinishedCache)

    // TEST: intended solution is correct against solution (and obviously against the hints, too)
    userGridYesStrict.change(0, 1, Filled())
    userGridYesStrict.change(1, 0, Filled())
    userGridNonStrict.change(0, 1, Filled())
    userGridNonStrict.change(1, 0, Filled())
    assertTrue(userGridYesStrict.isFinishedCache)
    assertTrue(userGridNonStrict.isFinishedCache)

    // TEST: non-intended solution is correct against hints, but not correct against solution
    userGridYesStrict.resetGame()
    userGridYesStrict.change(0, 0, Filled())
    userGridYesStrict.change(1, 1, Filled())
    userGridNonStrict.resetGame()
    userGridNonStrict.change(0, 0, Filled())
    userGridNonStrict.change(1, 1, Filled())
    assertFalse(userGridYesStrict.isFinishedCache)
    assertTrue(userGridNonStrict.isFinishedCache)

    // TEST: not correct against hints (number incorrect)
    userGridNonStrict.resetGame()
    userGridNonStrict.change(0, 0, Filled())
    assertFalse(userGridNonStrict.isFinishedCache)

    // TEST: not correct against hints (number correct, columns correct, rows incorrect)
    userGridNonStrict.resetGame()
    userGridNonStrict.change(0, 0, Filled())
    userGridNonStrict.change(0, 1, Filled())
    assertFalse(userGridNonStrict.isFinishedCache)

    // TEST: not correct against hints (number correct, columns incorrect, rows correct)
    userGridNonStrict.resetGame()
    userGridNonStrict.change(0, 0, Filled())
    userGridNonStrict.change(1, 0, Filled())
    assertFalse(userGridNonStrict.isFinishedCache)
  }
}