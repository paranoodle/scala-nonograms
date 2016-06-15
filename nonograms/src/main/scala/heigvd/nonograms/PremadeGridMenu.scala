package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object PremadeGridMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 150) / 2

  val button = new Button(272, 324, 150, 70, "no", NonogramsOffline.METRO_BLUE, PremadeGridMenu, () => {
  })
}
