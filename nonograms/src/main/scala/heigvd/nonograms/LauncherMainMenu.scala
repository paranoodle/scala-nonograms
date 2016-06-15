package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object LauncherMainMenu extends ScreenApp("Nonograms Scala Scage Game", 640, 480) with MultiController{
  backgroundColor = WHITE

  val x = (windowWidth - 150) / 2

  val random_grid_button = new Button(x, 324, 150, 70, "Random Grids", NonogramsOffline.METRO_BLUE, LauncherMainMenu, () => {
    RandomGridMenu.run()
  })

  val premade_grid_button = new Button(x, 224, 150, 70, "Premade Grids", NonogramsOffline.METRO_BLUE, LauncherMainMenu, () => {
    PremadeGridMenu.run()
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }
}
