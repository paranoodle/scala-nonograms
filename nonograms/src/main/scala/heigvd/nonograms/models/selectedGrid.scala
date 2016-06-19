package heigvd.nonograms.models

/**
  * Manages the selected grid in the current play.
  */
object SelectedGrid {
  var grid: Grid = new Grid()
  var userGrid: UserGrid = new UserGrid(grid)

  def setGrid(g: Grid, checkMode: Boolean = false) = {
    grid = g
    userGrid = new UserGrid(grid)
    userGrid.checkMode = checkMode
  }

  def getGrid() = grid

  def getUserGrid() = userGrid
}
