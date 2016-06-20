package heigvd.nonograms.models

import junit.framework.Assert._
import junit.framework._

/**
  * Tests for User and Selected Grid
  */
class ModelsTest extends TestCase {

  def testUser (): Unit = {
    val u = User
    val default = u.userID
    assertEquals(default, u.getUser)

    val username = "myuser"
    u.current = username
    assertEquals(username, u.current)
    assertEquals(username, u.getUser)
  }

  def testSelectedGrid(): Unit = {
    val s = SelectedGrid
    val grid = new Grid
    s.setGrid(grid)
    assertEquals(grid, s.getGrid())
    assertEquals(false, s.getUserGrid().checkMode) // default is false

    s.setGrid(grid, true)
    assertEquals(true, s.getUserGrid().checkMode)

    s.setGrid(grid, false)
    assertEquals(false, s.getUserGrid().checkMode)
  }

}
