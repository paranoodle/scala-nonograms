package heigvd.nonograms

import heigvd.nonograms.models.{UserGrid, Grid}

/**
  * Simple main to test grid / usergrid classes without the UI.
  */
object myMainUserGrid {
  def main(args: Array[String]): Unit = {
    println("Hello from UserGrid main class")
    println("--- there is a grid ---")
    val g = new Grid
    g.printGrid()
    println("--- there are the hints ---")
    g.printHints()
    println("--- there is a random user solution ---")
    val ug = new UserGrid(g)
    ug.generateRandomState()
    ug.printMyGrid()
  }
}