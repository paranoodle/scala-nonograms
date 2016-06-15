package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object LauncherMainMenu extends ScreenApp("Nonograms Scala Scage Game", 800, 600) with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 200) / 2
  val y = windowHeight - x + 100

  val random_grid_button = new Button(x, y, 200, 70, "Random Grids", Colors.METRO_BLUE, LauncherMainMenu, () => {
    RandomGridMenu.run()
  })

  val premade_grid_button = new Button(x, y - 100, 200, 70, "Premade Grids", Colors.METRO_BLUE, LauncherMainMenu, () => {
    PremadeGridMenu.run()
  })

  var name_input = false
  val name_button: Button = new Button(x, y - 200, 200, 70, "Enter Username", Colors.METRO_ORANGE, LauncherMainMenu, () => {
    name_button.text = ""
    User.current = ""
    name_input = true
  })

  val how_button = new Button(x, y - 300, 200, 70, "How to play", Colors.METRO_YELLOW, LauncherMainMenu, () => {
    HowToMenu.run()
  })

  val accepted_keys = Map(KEY_1 -> "1", KEY_2 -> "2", KEY_3 -> "3", KEY_4 -> "4",
    KEY_5 -> "5", KEY_6 -> "6", KEY_7 -> "7", KEY_8 -> "8", KEY_9 -> "9",
    KEY_0 -> "0", KEY_Q -> "q", KEY_W -> "w", KEY_E -> "e", KEY_R -> "r",
    KEY_T -> "t", KEY_Y -> "y", KEY_U -> "u", KEY_I -> "i", KEY_O -> "o",
    KEY_P -> "p", KEY_A -> "a", KEY_S -> "s", KEY_D -> "d", KEY_F -> "f",
    KEY_G -> "g", KEY_H -> "h", KEY_J -> "j", KEY_K -> "k", KEY_L -> "l",
    KEY_Z -> "z", KEY_X -> "x", KEY_C -> "c", KEY_V -> "v", KEY_B -> "b",
    KEY_N -> "n", KEY_M -> "m")

  for (k <- accepted_keys.keys) {
    key(k, onKeyDown = {
      if (name_input) {
        name_button.text += accepted_keys(k)
      }
    })
  }

  key(KEY_BACK, onKeyDown = {
    if (name_input && (name_button.text != "")) {
      name_button.text = name_button.text.init
    }
  })

  key(KEY_RETURN, onKeyDown = {
    if (name_input) {
      name_input = false
      User.current = name_button.text
      name_button.text = "Change Username"
    }
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }

  render {
    if (User.current != "") {
      // TODO: change position
      print("You are currently playing as " + User.current, Vec(10, 450), BLACK)
    }
  }
}
