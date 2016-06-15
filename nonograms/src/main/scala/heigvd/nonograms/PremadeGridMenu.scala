package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object PremadeGridMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 200) / 2

  val button = new Button(x, 324, 200, 70, "no", NonogramsOffline.METRO_BLUE, PremadeGridMenu, () => {
  })

  val back_button = new Button(windowWidth - 110, 400, 100, 70, "Back", NonogramsOffline.METRO_ORANGE, PremadeGridMenu, () => {
    PremadeGridMenu.stop()
  })
}
