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
  
  val grid = List(List(RED, YELLOW), List(GREEN, BLUE))
  val sizeX = 2
  val sizeY = 2
  val gridSize = 15
  
  render {
    for (x <- 0 to sizeX) {
      val pos = 100 + (sizeX - x) * gridSize
      drawLine(Vec(100, pos), Vec(100 + gridSize * sizeY, pos), GRAY)
    }
    for (y <- 0 to sizeY) {
      val pos = 100 + (sizeY - y) * gridSize
      drawLine(Vec(pos, 100), Vec(pos, 100 + gridSize * sizeX), GRAY)
    }
    for (x <- 0 until sizeX; y <- 0 until sizeY) {
      val posX = 100 + 7 + ((y) * (gridSize))
      val posY = 100 - 7 + ((sizeX - x) * (gridSize))
      drawFilledRectCentered(Vec(posX, posY), 10, 10, grid(x)(y))
    }
  }
}
