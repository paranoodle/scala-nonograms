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

  backgroundColor = BLACK
  render {
    openglMove(windowSize/2)
    openglRotate(ang)
    print("Hello World!", Vec(-50, -5), GREEN)
  }
}
