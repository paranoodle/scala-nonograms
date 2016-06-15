package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object RandomGridMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 620) / 2
  val y = windowHeight - x - 80

  for (i <- 0 until 3; j <- 0 until 3) {
    val sizeX = (i+1) * 5
    val sizeY = (j+1) * 5
    new Button(x + (210 * j), y - (80 * i), 200, 70, sizeX + "x" + sizeY + " Grid", Colors.METRO_BLUE, RandomGridMenu, () => {
      selectedGrid.setGrid(new Grid(sizeX, sizeY))
      NonogramsOffline.run()
    })
  }

  val back_button = new Button(50, 50, 100, 70, "Back", Colors.METRO_ORANGE, RandomGridMenu, () => {
    RandomGridMenu.stop()
  })
}
