package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._
import com.github.dunnololda.scage.ScageScreenApp
import com.github.dunnololda.scage.support.tracer3.{CoordTracer, DefaultTrace}
import com.github.dunnololda.scage.support.{ScageColor, Vec}

import scala.collection.mutable.ArrayBuffer

object NonogramsOffline extends ScageScreenApp("Nonograms", 640, 480) {
  private var ang = 0f
  actionStaticPeriod(100) {
    ang += 5
  }

  backgroundColor = WHITE

  var maybeStatus = false

  val g = (new Grid)
  g.printGrid()
  g.printHints()

  val userGrid = new UserGrid(g)
  userGrid.generateRandomState() // RANDOM state to test!
  userGrid.printMyGrid()
  val userSol = userGrid.userSolution

  val grid = g.solution
  val sizeX = g.sizeX
  val sizeY = g.sizeY
  val rowHintMax = g.rows_hint.map(_.size).max
  val colHintMax = g.cols_hint.map(_.size).max

  val gridSpacing = 21
  val gridOffset = 10
  val fullSize = 18
  // The origin X is at the left of the grid (row hints to the left, grid to the right)
  // The origin Y is at the bottom of the grid (grid and col hints are above)
  // Both are designed to center [grid+hints] in the middle of the window.
  val originX = (windowWidth - (gridSpacing * (sizeX - rowHintMax))) / 2
  val originY = (windowHeight - (gridSpacing * (sizeY + colHintMax))) / 2

  render {
    // horizontal grid lines
    val lenHorizontal = gridSpacing * sizeX
    val lenHints = gridSpacing * rowHintMax
    for (y <- 0 to sizeY) {
      val pos = originY + (sizeY - y) * gridSpacing
      drawLine(Vec(originX - lenHints, pos), Vec(originX + lenHorizontal, pos), GRAY)
    }

    // vertical grid lines
    val lenVertical = gridSpacing * (sizeY + colHintMax)
    for (x <- 0 to sizeX) {
      val pos = originX + (sizeX - x) * gridSpacing
      drawLine(Vec(pos, originY), Vec(pos, originY + lenVertical), GRAY)
    }

    // writing row hints
    for (row <- 0 until g.rows_hint.size; hint <- 0 until g.rows_hint(row).size) {
      val posX = originX + gridOffset - ((g.rows_hint(row).size - hint) * gridSpacing)
      val posY = originY - gridOffset + 2 + (sizeY - row) * gridSpacing
      print(g.rows_hint(row)(hint), Vec(posX, posY), BLACK, align = "center")
    }
    // writing column hints
    for (col <- 0 until g.cols_hint.size; hint <- 0 until g.cols_hint(col).size) {
      val posX = originX + gridOffset + col * gridSpacing
      val posY = originY - gridOffset + 2 + ((sizeY + g.cols_hint(col).size - hint) * gridSpacing)
      print(g.cols_hint(col)(hint), Vec(posX, posY), BLACK, align = "center")
    }

    // filled squares for solution
    for (x <- 0 until sizeX; y <- 0 until sizeY) {
      if (grid(x)(y)) {
        val posX = originX + gridOffset + (x * (gridSpacing))
        val posY = originY - gridOffset + ((sizeY - y) * (gridSpacing))
        drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, BLACK)
      }
    }

    // filled squares for mygrid current game
    for (x <- 0 until sizeX; y <- 0 until sizeY) {
      val (posX,posY) = arrayToScreen(x,y,gridOffset)

      // to hide solution (maybe to remove)
      drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, WHITE)

      userSol(x)(y) match {
        case Empty() =>
          print("X", Vec(posX, posY + 2), BLACK, align = "center")
        case Filled() =>
          drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, DARK_GRAY)
        case None() =>
        // does nothing
        case MaybeEmpty() =>
          print("?", Vec(posX, posY + 2), RED, align = "center")
        case MaybeFilled() =>
          drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, RED)
          print("?", Vec(posX, posY + 2), WHITE, align = "center")
      }
    }
  }

  leftMouse(onBtnDown = {m =>
    println("LEFT CLICK!! " + m)
    val (x,y) = screenToArray(m.x, m.y)
    if (maybeStatus) {
      userSol(x)(y) = userSol(x)(y) match {
        case None() => MaybeFilled()
        case MaybeEmpty() => None()
        case MaybeFilled() => None()
        case _ => userSol(x)(y)
      }
    } else {
      userSol(x)(y) = userSol(x)(y) match {
        case None() => Filled()
        case Empty() => None()
        case Filled() => None()
        case _ => userSol(x)(y)
      }
    }
  })

  rightMouse(onBtnDown = {m =>
    println("RIGHT CLICK!! " + m)
    val (x,y) = screenToArray(m.x, m.y)
    if (maybeStatus) {
      userSol(x)(y) = userSol(x)(y) match {
        case None() => MaybeEmpty()
        case MaybeEmpty() => None()
        case MaybeFilled() => None()
        case _ => userSol(x)(y)
      }
    } else {
      userSol(x)(y) = userSol(x)(y) match {
        case None() => Empty()
        case Empty() => None()
        case Filled() => None()
        case _ => userSol(x)(y)
      }
    }
  })

  def arrayToScreen(x: Int, y: Int, offset: Int = 0): (Int, Int) = {
    (
      originX + offset + (x * (gridSpacing)),
      originY - offset + ((sizeY - y) * (gridSpacing))
      )
  }

  def screenToArray(xs: Double, ys: Double, offset: Int = 0): (Int, Int) = {
    (
      ((xs - originX - offset) / gridSpacing).floor.toInt,
      (sizeY - ((ys - originY + offset) / gridSpacing)).floor.toInt
      )
  }
}
