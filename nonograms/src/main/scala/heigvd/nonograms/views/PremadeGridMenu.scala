package heigvd.nonograms.views

import com.github.dunnololda.scage.ScageLib._
import heigvd.nonograms.models.{SelectedGrid, Grid}
import heigvd.nonograms.utils.{Colors, Button, ToggleButton}

/**
  * View to select a pre-saved grid, with a given image to discover.
  */
object PremadeGridMenu extends Screen() with MultiController {
  backgroundColor = WHITE

  var checkMode = true

  val x = (windowWidth - 200) / 2
  val y = windowHeight - 170

  val button1 = new Button(x, y, 200, 70, xml("grid.name.cat") + " (10x10)", Colors.METRO_BLUE, PremadeGridMenu, () => {
    SelectedGrid.setGrid(new Grid(List(
      "0000000011", "0000000111", "1000100110", "1111100111", "1010100011",
      "1111111111", "1101111111", "0111111111", "0111000101", "0101000101"
    ), 1, true), checkMode)
    NonogramsOffline.run()
  })

  val button2 = new Button(x, y - 80, 200, 70, xml("grid.name.cat") + " (10x9)", Colors.METRO_BLUE, PremadeGridMenu, () => {
    SelectedGrid.setGrid(new Grid(List(
      "0000010001", "1100011111", "1000011111", "1000001111", "1000000100",
      "1000111110", "1001111110", "1011111110", "1111111101"
    ), 1, true), checkMode)
    NonogramsOffline.run()
  })

  val button3 = new Button(x, y - 160, 200, 70, xml("grid.name.cat") + " (15x15)", Colors.METRO_BLUE, PremadeGridMenu, () => {
    SelectedGrid.setGrid(new Grid(List(
      "111111110111111", "111111100001111", "111111010011111",
      "111101000001111", "000100110011111", "000010001111110",
      "000001000011101", "000001000011101", "000001000001001",
      "000001100000011", "000001100000111", "000001110001111",
      "000001111001111", "000001111001111", "000001110101111"
    ), 1, true), checkMode)
    NonogramsOffline.run()
  })

  val back_button = new Button(50, 50, 200, 70, xml("button.back"), Colors.METRO_ORANGE, PremadeGridMenu, () => {
    PremadeGridMenu.stop()
  })

  val strict_button: ToggleButton = new ToggleButton(windowWidth - 250, 50, 200, 70, Colors.METRO_RED, GRAY, xml("button.tostrict"), xml("button.instrict"), PremadeGridMenu, () => {
    checkMode = true
    free_button.activate()
  })
  strict_button.deactivate()

  val free_button: ToggleButton = new ToggleButton(windowWidth - 250, 130, 200, 70, Colors.METRO_GREEN, GRAY, xml("button.tofree"), xml("button.infree"), PremadeGridMenu, () => {
    checkMode = false
    strict_button.activate()
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }
}
