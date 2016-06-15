package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object RandomGridMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 150) / 2

  val button_5 = new Button(x, 324, 150, 70, "5x5 Grid", NonogramsOffline.METRO_BLUE, RandomGridMenu, () => {
    selectedGrid.setGrid(new Grid(5))
    NonogramsOffline.run()
  })

  val button_10 = new Button(x, 224, 150, 70, "10x10 Grid", NonogramsOffline.METRO_BLUE, RandomGridMenu, () => {
    selectedGrid.setGrid(new Grid(10))
    NonogramsOffline.run()
  })

  val button_15 = new Button(x, 124, 150, 70, "15x15 Grid", NonogramsOffline.METRO_BLUE, RandomGridMenu, () => {
    selectedGrid.setGrid(new Grid(15))
    NonogramsOffline.run()
  })

  val back_button = new Button(windowWidth - 110, 400, 100, 70, "Back", NonogramsOffline.METRO_ORANGE, NonogramsOffline, () => {
    NonogramsOffline.stop()
  })
}
