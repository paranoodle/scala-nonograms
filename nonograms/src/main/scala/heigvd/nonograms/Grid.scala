package heigvd.nonograms

/**
  * Created by val on 25/05/16.
  */
class Grid {

  val default_size = 10
  type Number = Int
  type Bool = Boolean
  var rows_hint: List[List[Number]] = List(List())
  var cols_hint: List[List[Number]] = List(List())
  var solution: List[List[Bool]] = List(List())

  def generateGrid(sizeX: Int = default_size, sizeY: Int = default_size) = {
    val r = scala.util.Random
    for (x <- 0 until sizeX) {

      var listy: List[Bool] = List()
      for (y <- 0 until sizeY) {
        listy = r.nextBoolean() :: listy
      }
      solution = listy :: solution
    }
    //println(solution.size)
    //println(solution(0).size)

    generateSolutionRow()
    generateSolutionCol()
  }

  def generateSolutionRow() = {
    // rows
    for (x <- solution) {

      // reset
      var listy: List[Number] = List()
      var last = false
      var num = 0

      for (y <- x) {
        if (y != last) {
          if (!y) {
            listy = num :: listy
          }
          last = y
          num = 1
        } else {
          num += 1
        }
      }
      if (last) {
        listy = num :: listy
      }

      rows_hint = rows_hint :+ listy.reverse
    }
  }

  def generateSolutionCol() {
    // columns
    //println(solution.size)
    //println(solution(0).size)

    for (xi <- 0 until solution(0).size) {

      // reset
      var listy: List[Number] = List()
      var last = false
      var num = 0

      for (yi <- 0 until solution.size - 1) {
        // -1 coz of empty list at beginning
        println(yi + " " + xi)
        val y = solution(yi)(xi)
        if (y != last) {
          if (!y) {
            listy = num :: listy
          }
          last = y
          num = 1
        } else {
          num += 1
        }
      }
      if (last) {
        listy = num :: listy
      }

      cols_hint = cols_hint :+ listy.reverse
    }

  }

  def print(): Unit = {
    for (x <- solution) {
      for (y <- x) {
        if (y)
          printf("X")
        else
          printf("-")
      }
      println()

    }

    val r = Range(0, 2)
    for (xx <- r) {
      println(xx)
    }
  }

  def printSol(): Unit = {
    for (s <- rows_hint) {
      for (ss <- s) {
        printf("%d ", ss)
      }
      println()
    }

    for (s <- cols_hint) {
      for (ss <- s) {
        printf("%d ", ss)
      }
      println()
    }
  }


}

object myMain {
  def main(args: Array[String]): Unit = {
    println("Hello from main of class")
    val g = (new Grid)
    g.generateGrid()
    g.print()
    g.printSol()
  }
}
