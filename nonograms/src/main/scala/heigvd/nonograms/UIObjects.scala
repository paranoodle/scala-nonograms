package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

import NonogramsOffline._

class Button(x: Int, y: Int, width: Int, height: Int, var text: String = "", var color: ScageColor = GRAY, var active: Boolean = true) {
  val tly = y + height

  def checkCollision(cx: Double, cy: Double): Boolean = {
    (cx > x) && (cx < x + width) && (cy > y) && (cy < y + height)
  }

  private val render_id = render {
    drawFilledRect(Vec(x, tly), width, height, color)
    print(text, Vec(x+(width/2), y+(height/2)), WHITE, align="center")
  }
}
