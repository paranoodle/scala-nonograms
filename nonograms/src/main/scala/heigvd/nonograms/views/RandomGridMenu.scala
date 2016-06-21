package heigvd.nonograms.views

import com.github.dunnololda.scage.ScageLib._
import heigvd.nonograms.models.{SelectedGrid, Grid}
import heigvd.nonograms.utils.{Colors, Button, ToggleButton}

/**
  * View to select the size of the random grid.
  */
object RandomGridMenu extends Screen() with MultiController {
  backgroundColor = WHITE

  var checkMode = false

  val x = (windowWidth - 620) / 2
  val y = windowHeight - x - 80

  for (i <- 0 until 3; j <- 0 until 3) {
    val sizeX = (i+1) * 5
    val sizeY = (j+1) * 5
    new Button(x + (210 * j), y - (80 * i), 200, 70, sizeX + "x" + sizeY + xml("main.grid"), Colors.METRO_BLUE, RandomGridMenu, () => {
      SelectedGrid.setGrid(new Grid(sizeX, sizeY), checkMode)
      NonogramsOffline.run()
    })
  }

  val back_button = new Button(50, 50, 200, 70, xml("button.back"), Colors.METRO_ORANGE, RandomGridMenu, () => {
    RandomGridMenu.stop()
  })

  val strict_button: ToggleButton = new ToggleButton(windowWidth - 250, 50, 200, 70, Colors.METRO_RED, GRAY, xml("button.tostrict"), xml("button.instrict"), RandomGridMenu, () => {
    checkMode = true
    free_button.activate()
  })

  val free_button: ToggleButton = new ToggleButton(windowWidth - 250, 130, 200, 70, Colors.METRO_GREEN, GRAY, xml("button.tofree"), xml("button.infree"), RandomGridMenu, () => {
    checkMode = false
    strict_button.activate()
  })
  free_button.deactivate()

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }
}
