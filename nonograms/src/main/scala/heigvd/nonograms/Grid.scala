package heigvd.nonograms

/**
  * Manage a grid with its solution and hints.
  *
  * Can be created with a existing List[List[Boolean]] or randomly generated (with default or specified size)
  * Hints will be created automatically based on the solution.
  */

class Grid(any:Boolean) {

  type Number = Int
  type Bool = Boolean
  type Hints = List[List[Number]]
  type Solution = List[List[Boolean]]
  var rows_hint: Hints = List()
  var cols_hint: Hints = List()
  var solution: Solution = List()

  def sizeX = solution.size
  def sizeY = if (solution.size == 0) 0 else solution.map(_.size).max

  // creates a grid with given values, and generates hints
  def this(sol: List[List[Boolean]]) = {
    this(true)
    solution = sol
    require(sizeX > 0)
    require(sizeY > 0)
    generateHints()
  }

  // generates a random grid of default size 10x8
  def this() {
    this(true)
    generateRandom(10, 8)
  }

  // generates a square random grid of specified size
  def this(sized:Int) {
    this(true)
    require(sized > 0)
    generateRandom(sized, sized)
  }

  // generates a rectangular random grid of specified size
  def this(sizeX: Int, sizeY: Int) = {
    this(true)
    require(sizeX > 0)
    require(sizeY > 0)
    generateRandom(sizeX, sizeY)
  }

  // generates a rectangular random grid of specified size
  private def generateRandom(sizeX: Int, sizeY: Int) = {
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
