package heigvd.nonograms

import junit.framework._
import Assert._

object NonogramsAppTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[NonogramsAppTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

/**
  * Unit test for simple App.
  */
class NonogramsAppTest extends TestCase("app") {

  def testRandomGridSize() = {
    // rectangular grid
    val x = 2
    val y = 3
    val g = new Grid(x, y)
    assertEquals(x, g.sizeX)
    assertEquals(y, g.sizeY)

    // square grid
    val h = new Grid(y)
    assertEquals(y, h.sizeX)
    assertEquals(y, h.sizeY)

    // default sized grid
    val i = new Grid
    assertEquals(10, i.sizeX)
    assertEquals(8, i.sizeY)
  }

  def testMyGridRowCol() = {
    // grid from specified content
    val s = List(
      List(false, false, false),
      List(false, false, true),
      List(false, true, false),
      List(false, true, true),
      List(true, false, false),
      List(true, false, true),
      List(true, true, false),
      List(true, true, true))
    val cols = List(
      List(),
      List(1),
      List(1),
      List(2),
      List(1),
      List(1, 1),
      List(2),
      List(3))
    val rows = List(
      List(4),
      List(2, 2),
      List(1, 1, 1, 1)
    )
    val g = new Grid(s)
    assertEquals(rows, g.rows_hint)
    assertEquals(cols, g.cols_hint)
    assertEquals(s(0).size, g.sizeY)
    assertEquals(s.size, g.sizeX)

    assertEquals(false, g.solution(6)(2))
    assertEquals(true, g.solution(4)(0))
  }


}
