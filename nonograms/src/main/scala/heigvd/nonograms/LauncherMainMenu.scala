package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object LauncherMainMenu extends ScreenApp("Nonograms Scala Scage Game", 640, 480) with MultiController{
  backgroundColor = WHITE

  val bt = new Button(40, 40, 50, 50, "New game", RED, () => {
    println("Click new button")
    NonogramsOffline.run()
  })
  val tbt = new ToggleButton(40, 80, 50, 50, BLUE, RED, "New", "Not New", () => {
    println("Click new toggle")
    NonogramsOffline.run()
  })

  val new_game_button = new sButton(xml("button.newgame"), Vec(312, 284) + Vec(-40, 40), 100, LauncherMainMenu, {
    println("Click new game provided button")
    NonogramsOffline.run()
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }

  render {
    // nothing important...
    print("Hello", Vec(400, 140), BLACK, align = "center")
    drawRect(Vec(380, 137), 40, 20, BLACK)
  }

  leftMouse(onBtnDown = { m =>
    tbt.click(m)
    bt.click(m)
  })

}

// COPY PASTED FROM ANOTHER PROJECT: SCAGE-PROJECT BLASES
class sButton(message: => String,
              coord: Vec,
              width: Int,
              screen: Screen with MultiController,
              onBtnPressed: => Any,
              color: ScageColor = BLACK,
              var visible: Boolean = true) /* extends IntersectablePolygon */ {
  def intersectableVertices = List(Vec(coord) + Vec(-5, 20),
    Vec(coord) + Vec(-5 + width, 20),
    Vec(coord) + Vec(-5 + width, -10),
    Vec(coord) + Vec(-5, -10))

  screen.interface {
    if (visible) {
      print(message, Vec(coord), color)
    }
  }

  screen.leftMouseOnAreaIgnorePause(intersectableVertices, onBtnDown = { m => if (visible /* && containsCoord(m)*/ ) onBtnPressed })
}
