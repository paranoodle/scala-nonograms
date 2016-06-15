package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object PremadeGridMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 200) / 2

  val button = new Button(x, 324, 200, 70, "Cat (10x10)", Colors.METRO_BLUE, PremadeGridMenu, () => {
    selectedGrid.setGrid(new Grid(List(
      "0000000011",
      "0000000111",
      "1000100110",
      "1111100111",
      "1010100011",
      "1111111111",
      "1101111111",
      "0111111111",
      "0111000101",
      "0101000101"
    ), 1))
    NonogramsOffline.run()
  })

  val back_button = new Button(windowWidth - 110, 400, 100, 70, "Back", Colors.METRO_ORANGE, PremadeGridMenu, () => {
    PremadeGridMenu.stop()
  })
}
