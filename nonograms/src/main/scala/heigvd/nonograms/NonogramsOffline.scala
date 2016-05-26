package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._
import com.github.dunnololda.scage.ScageScreenApp
import com.github.dunnololda.scage.support.tracer3.{CoordTracer, DefaultTrace}
import com.github.dunnololda.scage.support.{ScageColor, Vec}

import scala.collection.mutable.ArrayBuffer

object NonogramsOffline extends ScageScreenApp("Scage App", 640, 480) {
  private var ang = 0f
  actionStaticPeriod(100) {
    ang += 5
  }

  backgroundColor = WHITE
  
  val g = (new Grid)
  g.generateGrid()
  g.print()
  g.printSol()
  
  //val grid = List(List(RED, YELLOW), List(GREEN, BLUE))
  val grid = g.solution
  val sizeX = g.default_size
  val sizeY = g.default_size
  val rowHintMax = g.rows_hint.map(_.size).max
  val colHintMax = g.cols_hint.map(_.size).max
  
  val gridSpacing = 21
  val gridOffset = 10
  val fullSize = 18
  val originX = (windowWidth - (gridSpacing * (sizeX + rowHintMax))) / 2
  val originY = (windowHeight - (gridSpacing * (sizeY + colHintMax))) / 2
  
  render {
    // horizontal grid lines
    val lenHorizontal = gridSpacing * sizeY
    val lenHints = gridSpacing * rowHintMax
    for (x <- 0 to sizeX) {
      val pos = originY + (sizeX - x) * gridSpacing
      drawLine(Vec(originX - lenHints, pos), Vec(originX + lenHorizontal, pos), GRAY)
    }
    
    // vertical grid lines
    val lenVertical = gridSpacing * (sizeX + colHintMax)
    for (y <- 0 to sizeY) {
      val pos = originX + (sizeY - y) * gridSpacing
      drawLine(Vec(pos, originY), Vec(pos, originY + lenVertical), GRAY)
    }
    
    // writing row hints
    for (row <- 0 until g.rows_hint.size; hint <- 0 until g.rows_hint(row).size) {
      val posX = originX + gridOffset - ((g.rows_hint(row).size - hint) * gridSpacing)
      val posY = originY + gridOffset + 2 + (sizeX - row) * gridSpacing
      print(g.rows_hint(row)(hint), Vec(posX, posY), BLACK, align="center")
    }
    // writing column hints
    for (col <- 0 until g.cols_hint.size; hint <- 0 until g.cols_hint(col).size) {
      val posX = originX - gridOffset + col * gridSpacing
      val posY = originY - gridOffset + 2 + ((sizeX + g.cols_hint(col).size - hint) * gridSpacing)
      print(g.cols_hint(col)(hint), Vec(posX, posY), BLACK, align="center")
    }
    
    // filled squares
    for (x <- 0 until sizeX; y <- 0 until sizeY) {
      if (grid(x)(y)) {
        val posX = originX + gridOffset + (y * (gridSpacing))
        val posY = originY - gridOffset + ((sizeX - x) * (gridSpacing))
        drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, BLACK)
      }
    }
  }
}
