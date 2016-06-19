package heigvd.nonograms

import heigvd.nonograms

/**
  * Grid currently played by a user, with game state.
  *
  * WARNING: THE WHOLE CLASS IS BASED ON CACHED VALUES.
  * IT DOES NOT UPDATE AUTOMATICALLY WHEN CALLED, USE IT ONLY FOR RENDERING PURPOSES

  * Only speed-up the rendering process, the renderer can't call the internal class
  * (eg. to check if the game is finished, the number of cells that are filled, etc..)
  * Instead, these values are kept in cache. Each time the UI makes a change to the model
  * THROUGH this class, the cached values updates.
  */
class UserGrid(grid: Grid) {

  // by default, non-strict mode.
  var checkMode = false

  // ********** TIMERS ********** //

  // start time: set up and freeze when first called
  private lazy val time_start_init:Long = System.currentTimeMillis()
  // start time: user-defined override (used by reset, ASSIGN TO CURRENT TIME TO RESET)
  private var time_start_override:Long = 0
  // get the start time: to be called from the outside
  def time_start():Long = {
    if (time_start_override != 0) {
      time_start_override
    } else {
      time_start_init
    }
  }

  /* Return the elapsed time since the class was launched */
  def time_elapsed:Long = System.currentTimeMillis() - time_start

  // implementation of a "reassignable lazy val"
  // end time: set up and freeze when first called (ASSIGN TO ZERO TO RESET)
  private var time_finished_override:Long = 0
  // end time: set up and freeze when first called
  def time_finished (): Long = {
    if (isFinishedCache) {
      if (time_finished_override == 0) {
        time_finished_override = System.currentTimeMillis() - time_start
      }
      time_finished_override
    } else {
      time_elapsed
    }
  }

  // ********** USER SOLUTION ********** //

  // the array of cell state of the current game
  private var userSolution: Array[Array[CellType]] = Array.fill[CellType](grid.sizeX, grid.sizeY)(nonograms.None())

  // get the state of a single cell (getter)
  def getUserSolution(x: Int)(y:Int) : CellType = {
    userSolution(x)(y)
  }

  // change the state of a single cell (setter) & update cache
  def change(x: Int, y: Int, state: CellType): Unit = {
    userSolution(x)(y) = state match {
      case Tried() => nonograms.None()
      case _ => state
    }
    // updates the cache
    updateCache()
  }

  // ********** HANDLING CLICKS & Update cache ********** //

  def leftClick(x: Int, y: Int, maybeStatus: Boolean = false) = {
    if (maybeStatus) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case None() => MaybeFilled()
        case MaybeEmpty() => nonograms.None()
        case MaybeFilled() => nonograms.None()
        case _ => userSolution(x)(y)
      }
    } else {
      val valid = !checkMode || (checkMode && grid.solution(x)(y))
      println(valid)
      userSolution(x)(y) = userSolution(x)(y) match {
        case None() => if (valid) Filled() else Tried()
        case Empty() => nonograms.None()
        case Filled() => nonograms.None()
        case _ => userSolution(x)(y)
      }

      updateCache()
    }
  }

  def rightClick(x: Int, y: Int, maybeStatus: Boolean = false) = {
    if (maybeStatus) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case None() => MaybeEmpty()
        case MaybeEmpty() => nonograms.None()
        case MaybeFilled() => nonograms.None()
        case _ => userSolution(x)(y)
      }
    } else {
      userSolution(x)(y) = userSolution(x)(y) match {
        case None() => Empty()
        case Empty() => nonograms.None()
        case Filled() => nonograms.None()
        case _ => userSolution(x)(y)
      }
    }
  }

  // reset all "maybe" sign to None(), game is now to the last known stable state
  def removeAllMaybe() = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case MaybeEmpty() => nonograms.None()
        case MaybeFilled() => nonograms.None()
        case _ => userSolution(x)(y)
      }
    }
    // updates the cache
    updateCache()
  }

  // validates all the "maybe" states to their corresponding stable state
  def validateAllMaybe() = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case MaybeEmpty() => Empty()
        case MaybeFilled() => {
          if (checkMode && !grid.solution(x)(y)) Tried()
          else Filled()
        }
        case _ => userSolution(x)(y)
      }
    }
    // updates the cache
    updateCache()
  }

  // reset the game to the starting point (all None)
  def resetGame() = {
    userSolution = Array.fill[CellType](grid.sizeX, grid.sizeY)(nonograms.None())

    // updates the cached values
    time_start_override = System.currentTimeMillis()
    checkGameIsFinished()
    numberFilled()
    numberPenalties()
    time_finished_override = 0
  }



  // Util method to transpose a CellType to Boolean (default: false)
  private def fromCellTypeToBoolean(c:CellType): Boolean = c match {
    case Empty() => false
    case Filled() => true
    case _ => false
  }

  // updates all the caches
  private def updateCache(): Unit = {
    checkGameIsFinished(checkMode)
    numberFilled()
    numberPenalties()
  }

  // ********** METRICS ********** //

  // return the cached value of number of filled cells
  var numberFilledCache = numberFilled()
  // count the number of filled cells and updates the cache
  private def numberFilled (): Int = {
    numberFilledCache =grid.numberFilled(userSolution.flatten.map(c => fromCellTypeToBoolean(c)).toList)
    numberFilledCache
  }

  // return the cached value of penalties
  var numberPenaltiesCache = numberPenalties()
  // count the penalties and updates the cache
  private def numberPenalties (): Int = {
    numberPenaltiesCache = grid.numberFilled(userSolution.flatten.map(c => c match {
      case Tried() => true
      case _ => false
    }).toList)
    numberPenaltiesCache
  }

  /* penalty time be error */
  private val penalty_time = 20000;
  /* penalties time calculation */
  def penaltiesTime = numberPenaltiesCache * penalty_time;

  // ********** GAME FINISHED ********** //

  /**
    * Stores if the game is finished (cache)
    */
  var isFinishedCache = false
  /**
  * Check that the game is finished and valid, by default or false against the hints, if true either against the solution.
  * */
  private def checkGameIsFinished(checkMode: Boolean = false) = {
    // check the finished status and updates the cache
    isFinishedCache = checkMode match {
      case true => checkGameFinishedAgainstSolution()
      case false => checkGameFinishedAgainstHints()
    }
    isFinishedCache
  }

  /**
    * Check that the game is finished and valid against the solution (not against the hints).
    *
    * A completed game has only Empty and Filled cells (no other state like None or ...Maybe).
    * Moreover, Empty cells must be empty (false) in solution, and Filled cells filled (true) in solution.
    *
    * To remove this last behavior and check ONLY that is has only Empty and Filled cells,
    * set againstSolution to false (the game is not garantueed to be finished, then).
    *
    * @return Return true if game is completed and valid, false otherwise.
    */
  private def checkGameFinishedAgainstSolution(againstSolution:Boolean = true): Boolean = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) match {
        case Empty()|None()|Tried() => if (againstSolution && grid.solution(x)(y)) return false
        case Filled() => if (againstSolution && !grid.solution(x)(y)) return false
        case _ => return false
      }
    }
    true
  }

  /**
    * Check that the game is finished and valid against the hints (not against the solution).
    *
    * A completed game has only Empty and Filled cells (no other state like None or ...Maybe).
    * Moreover, the hints for rows and colums must corresponding to the user solution
    * MUST be the same hints has the one from the solution. The user solution and grid solution
    * MIGHT be different, though.
    *
    * @return Return true if game is completed and valid, false otherwise.
    */
  private def checkGameFinishedAgainstHints() : Boolean = {
    // first, must consist ONLY of filled/empty cells: checkGameFinishedAgainstSolution(false)
    // actually, we only care the number of filled cells in user solution is equal to the number of true cells in grid solution
    if (grid.numberFilled() != numberFilled) {
      return false
    }

    // then, rows and cols hints must be the same
    var my_cols_hint : List[List[Int]] = List()
    var my_rows_hint : List[List[Int]] = List()

    // check columns
    for (x <- userSolution)
      my_cols_hint :+= grid.generateHintsFromList((x map (c => fromCellTypeToBoolean(c))).toList)

    if (!my_cols_hint.equals(grid.cols_hint)) {
      return false
    }

    // check rows
    for (yi <- 0 until grid.sizeY)
      my_rows_hint :+= grid generateHintsFromList (userSolution map (x => x(yi)) map(c => fromCellTypeToBoolean(c))).toList

    if (!my_rows_hint.equals(grid.rows_hint)) {
      return false
    }

    true
  }

  // ********** TESTS UTILITIES (Print & Random) ********** //

  // print the state, for testing purposes mainly
  def printMyGrid(): Unit = {
    println("--- My solution ---")
    for (yi <- 0 until grid.sizeY) {
      for (xi <- 0 until grid.sizeX) {
        val x = userSolution(xi)(yi) match {
          case Empty() => "E"
          case Filled() => "F"
          case None() => "N"
          case MaybeEmpty() => "m"
          case MaybeFilled() => "M"
          case Tried() => "!"
        }
        printf("%s", x)
      }
      println()
    }
  }

  // generate a random state (for testing GUI purposes)
  def generateRandomState() = {
    val r = scala.util.Random
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) = r.nextInt(6) match {
        case 0 => nonograms.None()
        case 1 => Empty()
        case 2 => Filled()
        case 3 => MaybeEmpty()
        case 4 => MaybeFilled()
        case 5 => Tried()
      }
    }
  }

}
