package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object LauncherMainMenu extends ScreenApp(xml("main.title"), 800, 600) with MultiController {
  backgroundColor = WHITE

  val x = (windowWidth - 200) / 2
  val y = windowHeight - x + 100

  val random_grid_button = new Button(x, y, 200, 70, xml("button.newrandom"), Colors.METRO_BLUE, LauncherMainMenu, () => {
    RandomGridMenu.run()
  })

  val premade_grid_button = new Button(x, y - 100, 200, 70, xml("button.newsaved"), Colors.METRO_BLUE, LauncherMainMenu, () => {
    PremadeGridMenu.run()
  })

  var name_input = false
  val name_button: Button = new Button(x, y - 200, 200, 70, xml("button.username.new"), Colors.METRO_ORANGE, LauncherMainMenu, () => {
    name_button.text = ""
    User.current = ""
    name_input = true
  })

  val how_button = new Button(x, y - 300, 200, 70, xml("button.howto"), Colors.METRO_YELLOW, LauncherMainMenu, () => {
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
      name_button.text = xml("button.username.change")
    }
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }

  render {
    var text = xml("text.username.youare") + xml("text.username.maybechange")
    if (User.current != "") {
      // easter egg with names from the students and teachers
      var todisplay = User.current
      // students in the class (10)
      if (User.current.matches(".*mig.*") || User.current.matches(".*san.*") || User.current.matches(".*jer.*") || User.current.matches(".*mor.*")) {
        todisplay = "I <3 Vaud 4 ever !"
      } else if (User.current.matches(".*val.*") || User.current.matches(".*min.*")) {
        todisplay = "#erasmus #wodka #poland #party"
      } else if (User.current.matches(".*para.*") || User.current.matches(".*eleo.*")) {
        todisplay = "I <3 Cats"
      } else if (User.current.matches(".*j[ia]m.*") || User.current.matches(".*nol.*")) {
        todisplay = "#erasmus #HolaChicas #fiesta #sangria"
      } else if (User.current.matches(".*mel.*") || User.current.matches(".*hk.*")) {
        todisplay = "I <3 Teakwondo"
      } else if (User.current.matches(".*yas.*") || User.current.matches(".*kam.*")) {
        todisplay = "report Over 9000 pages"
      } else if (User.current.matches(".*kar.*") || User.current.matches(".*goz.*")) {
        todisplay = "Vive Eurosport ;)"
      } else if (User.current.matches(".*dav.*") || User.current.matches(".*kun.*")) {
        todisplay = "#TheTanners #erasmus #norway"
      } else if (User.current.matches(".*[ijy]an.*") || User.current.matches(".*pur.*")) {
        todisplay = "I <3 Valais 4ever"
        // other students
      } else if (User.current.matches(".*bas.*") || User.current.matches(".*rou.*")) {
        todisplay = "#erasmus #Korea #mercilaDGES #"
      } else if (User.current.matches(".*ben.*") || User.current.matches(".*wol.*") || User.current.matches(".*cav.*")) {
        todisplay = "Expert Java #1"
      } else if (User.current.matches(".*leo.*") || User.current.matches(".*bern.*")) {
        todisplay = "#JeSuisLeonard"
      } else if (User.current.matches(".*fab.*") ||  User.current.matches(".*sal.*")) {
        todisplay = "#SeeYouNextYear"
      } else if (User.current.matches(".*sac.*") || User.current.matches(".*bro.*") || User.current.matches(".*cop.*") || User.current.matches(".*bin.*")) {
        todisplay = "#JeSaisPasCoder #JeSuisSB"
      } else if (User.current.matches(".*mic.*") || User.current.matches(".*bert.*")) {
        todisplay = "I <3 InstantStock, Vaud & Baleinev 4 ever"
      } else if (User.current.matches(".*ra[fp].*") || User.current.matches(".*mas.*")) {
        todisplay = "#CafeAddict I <3 Valais 4ever"
      } else if (User.current.matches(".*ste.*") || User.current.matches(".*don.*")) {
        todisplay = "I <3 Vaud & Baleinev 4ever"
      } else if (User.current.matches(".*ien.*") || User.current.matches(".*gar.*")) {
        todisplay = "I <3 MacOS 4ever"
        // teachers
      } else if (User.current.matches(".*nas.*") || User.current.matches(".*ate.*")) {
        todisplay = "I <3 Scala 4ever"
      } else if (User.current.matches(".*jon.*") || User.current.matches(".*bis.*")) {
        todisplay = "I <3 scala.JS 4ever "
      } else if (User.current.matches(".*ren.*")) {
        todisplay = "#ADA Implicitement Typiquement "
      } else if (User.current.matches(".*rob.*")) {
        todisplay = "best travel organizer ever"
      } else if (User.current.matches(".*oli.*") || User.current.matches(".*lie.*")) {
        todisplay = "I <3 web & JavaEE 4ever"
      } else if (User.current.matches(".*eri.*") || User.current.matches(".*fra.*")) {
        todisplay = "I <3 Swing 1.0 NEW! & Comic Sans 4ever"
      } else if (User.current.matches(".*art.*") || User.current.matches(".*ode.*")) {
        todisplay = "I created Scala, respect to the PGM you newbies!"
      }

      text = xml("text.username.youare") + todisplay
      if (!todisplay.equalsIgnoreCase(User.current)) {
        text += " (AKA " + User.current + ")"
      }
    }
    print(text, Vec(10, 580), BLACK)
  }
}
