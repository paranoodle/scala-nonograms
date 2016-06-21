package heigvd.nonograms.utils

import com.github.dunnololda.scage.ScageLib._

/**
  * Grafics Utils for buttons, toggle buttons, and progress bars.
  */

class Button(x: Int, y: Int, width: Int, height: Int,
    var text: String = "", var color: ScageColor = RED,
    screen: Screen with MultiController,
    onClick: () => Unit) {
  val tly = y + height

  def checkCollision(cx: Double, cy: Double): Boolean = {
    (cx > x) && (cx < x + width) && (cy > y) && (cy < y + height)
  }

  def click(m: Vec): Unit = {
    if (checkCollision(m.x,m.y)) onClick()
  }

  screen.interface {
    drawFilledRect(Vec(x, tly), width, height, color)
    print(text, Vec(x+(width/2), y+(height/2)), WHITE, align="center")
  }

  screen.leftMouseIgnorePause(onBtnDown = { m => click(m) })
}

class ToggleButton(x: Int, y: Int, width: Int, height: Int,
    activeColor: ScageColor = RED, inactiveColor: ScageColor = GRAY,
    activeText: String = "", inactiveText: String = "",
    screen: Screen with MultiController,
    onClick: () => Unit)
    extends Button(x, y, width, height, activeText, activeColor, screen, onClick) {
  var active = true

  override def click(m: Vec): Unit = {
    if (active && checkCollision(m.x,m.y)) {
      onClick()
      deactivate()
    }
  }

  def deactivate(): Unit = {
    color = inactiveColor
    text = inactiveText
    active = false
  }

  def activate(): Unit = {
    color = activeColor
    text = activeText
    active = true
  }
}

class ProgressBar(x: Int, y: Int, width: Int, height: Int,
    fillColor: ScageColor = BLACK, emptyColor: ScageColor = GRAY,
    var text: String = "", screen: Screen with MultiController) {
  val tly = y + height
  var progress: Double = 0.0

  screen.interface {
    drawFilledRect(Vec(x, tly), width, height, emptyColor)
    drawFilledRect(Vec(x, tly), (width * progress).toInt, height, fillColor)
    print(text, Vec(x+(width/2), y+(height/2)), WHITE, align="center")
  }
}
