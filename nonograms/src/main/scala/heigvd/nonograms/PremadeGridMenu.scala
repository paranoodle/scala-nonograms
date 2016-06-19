package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._
import heigvd.nonograms.HowToMenu._

object PremadeGridMenu extends Screen() with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 200) / 2

  val button = new Button(x, 324, 200, 70, xml("grid.name.cat") + " (10x10)", Colors.METRO_BLUE, PremadeGridMenu, () => {
    selectedGrid.setGrid(new Grid(List(
      "0000000011", "0000000111", "1000100110", "1111100111", "1010100011",
      "1111111111", "1101111111", "0111111111", "0111000101", "0101000101"
    ), 1, true), true)
    NonogramsOffline.run()
  })

  val back_button = new Button(50, 50, 200, 70, xml("button.back"), Colors.METRO_ORANGE, PremadeGridMenu, () => {
    PremadeGridMenu.stop()
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }
}
