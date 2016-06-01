package heigvd.nonograms

// Types of cells fot the current game played by a user.
sealed trait CellType;
// No current state (the default state for all cells)
case class None() extends CellType
// Current state is empty (the user believe the state is empty, and solution=false)
case class Empty() extends CellType
// Current state is filles (the user believe the state is filled, and solution=true)
case class Filled() extends CellType
// same as empty, but the user is not sure yet
case class MaybeEmpty() extends CellType
// same as filled, but the user is not sure yet
case class MaybeFilled() extends CellType

/**
  * Grid currently played by a user, with game state.
  */
class UserGrid(grid: Grid) {

  // the array of cell state of the current game
  var userSolution: Array[Array[CellType]] = Array.fill[CellType](grid.sizeX, grid.sizeY)(None())

  // change the state of a single cell
  def change(x: Int, y: Int, state: CellType): Unit = {
    userSolution(x)(y) = state
  }

  // reset all "maybe" sign to None(), game is now to the last known stable state
  def removeAllMaybe() = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case MaybeEmpty() => None()
        case MaybeFilled() => None()
        case _ => userSolution(x)(y)
      }
    }
  }

  // validates all the "maybe" states to their corresponding stable state
  def validateAllMaybe() = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) = userSolution(x)(y) match {
        case MaybeEmpty() => Empty()
        case MaybeFilled() => Filled()
        case _ => userSolution(x)(y)
      }
    }
  }

  // reset the game to the starting point (all None)
  def resetGame() = {
    userSolution = Array.fill[CellType](grid.sizeX, grid.sizeY)(None())
  }

  // Util method to transpose a CellType to Boolean (default: false)
  def fromCellTypeToBoolean(c:CellType): Boolean = c match {
    case Empty() => false
    case Filled() => true
    case _ => false
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
  def checkGameFinishedAgainstSolution(againstSolution:Boolean = true): Boolean = {
    for (x <- 0 until grid.sizeX; y <- 0 until grid.sizeY) {
      userSolution(x)(y) match {
        case Empty() => if (againstSolution && grid.solution(x)(y)) return false
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
  def checkGameFinishedAgainstHints() : Boolean = {
    // first, must consist ONLY of filled/empty cells: checkGameFinishedAgainstSolution(false)
    // actually, we only care the number of filled cells in user solution is equal to the number of true cells in grid solution
    if (grid.numberFilled() == grid.numberFilled(userSolution.flatten.map(c => fromCellTypeToBoolean(c)) toList)) {
      return false
    }

    // then, rows and cols hints must be the same
    var my_cols_hint : List[List[Int]] = List()
    var my_rows_hint : List[List[Int]] = List()

    // check columns
    for (x <- userSolution)
      my_cols_hint :+= grid.generateHintsFromList((x map (c => fromCellTypeToBoolean(c))) toList)

    if (my_cols_hint.equals(grid.cols_hint)) {
      return false
    }

    // check rows
    for (yi <- 0 until grid.sizeY)
      my_rows_hint :+= grid.generateHintsFromList(userSolution map (x => x(yi)) map(c => fromCellTypeToBoolean(c)) toList)

    if (my_rows_hint.equals(grid.rows_hint)) {
      return false
    }

    true
  }

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
      userSolution(x)(y) = r.nextInt(5) match {
        case 0 => None()
        case 1 => Empty()
        case 2 => Filled()
        case 3 => MaybeEmpty()
        case 4 => MaybeFilled()
      }
    }
  }

}

object myMainUserGrid {
  def main(args: Array[String]): Unit = {
    println("Hello from UserGrid main class")
    val g = new Grid
    val ug = new UserGrid(g)
    ug.generateRandomState()
    ug.printMyGrid()
  }
}
