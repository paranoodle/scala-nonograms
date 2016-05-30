package heigvd.nonograms

/**
  * Manage a grid with its solution and hints.
  *
  * Can be created with a existing List[List[Boolean]] or randomly generated.
  * Hints will be created automatically based on the solution.
  */

class Grid(var solution: List[List[Boolean]]) {
  generateHints()

  def sizeX = solution.size
  def sizeY = if (sizeX == 0) 0 else solution(0).size

  type Number = Int
  type Bool = Boolean
  type Hints = List[List[Number]]
  var rows_hint: Hints = List()
  var cols_hint: Hints = List()


  // generate a random grid of specified size (default: 10x8) and creates its hints
  def this(sizeX: Int = 10, sizeY: Int = 8) = {
    this(List())
    val r = scala.util.Random
    for (x <- 0 until sizeX) {

      var col: List[Bool] = List()
      for (y <- 0 until sizeY) {
        col = r.nextBoolean() :: col
      }
      solution = col :: solution
    }
    generateHints()
  }

  /**
    * Internal storage of an Intermediate IntermState, in order to create hints for the grid
    */
  private class IntermState {
    private var hints: List[Number] = List()
    private var last: Bool = false
    private var count = 0

    // update the intermediate state with the next boolean
    def nextState(current: Bool) = {
      if (current != last) {
        // change: report the "true" count in hints, reset last and count
        if (last) {
          hints = count :: hints
        }
        last = current
        count = 1
      } else {
        // no change: update count
        count += 1
      }
      this
    }

    // finish up (last element) and returns the hints list
    def finish() = {
      if (last) {
        hints = count :: hints
      }
      hints.reverse
    }
  }

  // generate the hints for existing solution
  private def generateHints() = {
    // columns
    for (x <- solution)
      cols_hint :+= x.foldLeft(new IntermState)((state, bool) => state.nextState(bool)).finish

    // rows
    for (yi <- 0 until sizeY)
      rows_hint :+= solution.map(x => x(yi)).foldLeft(new IntermState)((state, bool) => state.nextState(bool)).finish
  }

  // print out the grid (the solution)
  def printGrid(): Unit = {
    println("--- Grid solution ---")
    for (yi <- 0 until sizeY) {
      for (xi <- 0 until sizeX) {
        if (solution(xi)(yi)) printf("X") else printf("-")
      }
      println()
    }
  }

  // print out the hints
  def printHints(): Unit = {
    def printHints(coll: Hints) = {
      for (content <- coll) {
        for (sol <- content) {
          printf("%d ", sol)
        }
        println()
      }
    }

    println("--- Rows ---")
    printHints(rows_hint)

    println("--- Cols ---")
    printHints(cols_hint)
  }


}

object myMain {
  def main(args: Array[String]): Unit = {
    println("Hello from main of class")
    val g = new Grid
    g.printGrid()
    g.printHints()
  }
}
