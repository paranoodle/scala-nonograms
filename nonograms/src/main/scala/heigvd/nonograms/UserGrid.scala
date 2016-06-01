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
