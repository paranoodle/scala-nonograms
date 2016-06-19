package heigvd.nonograms.views

import com.github.dunnololda.scage.ScageLib._
import heigvd.nonograms.models.{SelectedGrid, Grid}
import heigvd.nonograms.utils.{Colors, Button}

/**
  * View to select the size of the random grid.
  */
object RandomGridMenu extends Screen() with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 620) / 2
  val y = windowHeight - x - 80

  for (i <- 0 until 3; j <- 0 until 3) {
    val sizeX = (i+1) * 5
    val sizeY = (j+1) * 5
    new Button(x + (210 * j), y - (80 * i), 200, 70, sizeX + "x" + sizeY + xml("main.grid"), Colors.METRO_BLUE, RandomGridMenu, () => {
      SelectedGrid.setGrid(new Grid(sizeX, sizeY))
      NonogramsOffline.run()
    })
  }

  val back_button = new Button(50, 50, 200, 70, xml("button.back"), Colors.METRO_ORANGE, RandomGridMenu, () => {
    RandomGridMenu.stop()
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }
}
