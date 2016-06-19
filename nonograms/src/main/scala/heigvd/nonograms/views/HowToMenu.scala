package heigvd.nonograms.views

import com.github.dunnololda.scage.ScageLib._
import heigvd.nonograms.utils.{Colors, Button}

/**
  * Flexible number of view(s) to explain the game or show information.
  */
object HowToMenu extends Screen() with MultiController {
  backgroundColor = WHITE

  var page_index = 0
  val page_max = 2

  val previousButton: Button = new Button(50, 50, 200, 70, xml("button.backmenu"), Colors.METRO_YELLOW, HowToMenu, () => {
    if (page_index > 0) {
      page_index -= 1

      nextButton.color = Colors.METRO_ORANGE
      nextButton.text = xml("button.next")

      if (page_index == 0) {
        previousButton.color = Colors.METRO_YELLOW
        previousButton.text = xml("button.backmenu")
      }
    } else if (page_index == 0) {
      stop()
    }
  })

  val nextButton: Button = new Button(windowWidth - 250, 50, 200, 70, xml("button.next"), Colors.METRO_ORANGE, HowToMenu, () => {
    if (page_index < page_max) {
      page_index += 1

      previousButton.color = Colors.METRO_ORANGE
      previousButton.text = xml("button.back")

      if (page_index == page_max) {
        nextButton.color = Colors.METRO_GREEN
        nextButton.text = xml("button.returnmenu")
      }
    } else if (page_index == page_max) {
      // reset to default
      page_index = 0
      previousButton.color = Colors.METRO_YELLOW
      previousButton.text = xml("button.backmenu")
      nextButton.color = Colors.METRO_ORANGE
      nextButton.text = xml("button.next")
      stop()
    }
  })

  interface {
    print(xml("launcher.info"), 10, 10, BLACK)
  }

  render {
    val content = page_index match {
      case 0 => xml("help.text0")
      case 1 => xml("help.text1")
      case 2 => xml("help.text2")
    }

    print(content, Vec(100, 500), BLACK)
  }
}
