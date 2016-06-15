package heigvd.nonograms

import com.github.dunnololda.scage.ScageLib._

object HowToMenu extends Screen("Nonograms") with MultiController {
  backgroundColor = WHITE

  var page_index = 0
  val page_max = 1

  val previousButton: Button = new Button(50, 50, 200, 70, "Back to Menu", Colors.METRO_YELLOW, HowToMenu, () => {
    if (page_index > 0) {
      page_index -= 1

      nextButton.color = Colors.METRO_ORANGE
      nextButton.text = "Next"

      if (page_index == 0) {
        previousButton.color = Colors.METRO_YELLOW
        previousButton.text = "Back to Menu"
      }
    } else if (page_index == 0) {
      stop()
    }
  })

  val nextButton: Button = new Button(windowWidth - 250, 50, 200, 70, "Next", Colors.METRO_ORANGE, HowToMenu, () => {
    if (page_index < page_max) {
      page_index += 1

      previousButton.color = Colors.METRO_ORANGE
      previousButton.text = "Back"

      if (page_index == page_max) {
        nextButton.color = Colors.METRO_GREEN
        nextButton.text = "Return to Menu"
      }
    } else if (page_index == page_max) {
      page_index = 0
      stop()
    }
  })

  render {
    val content = page_index match {
      case 0 => "Nonograms are a type of logic puzzle, where the aim is to color\nthe cells in the grid according to the given numbers.\n\nPer example, a line listed as '2 1' would consist of:\n- 2 touching filled squares\n- followed by at least one space\n- and then another filled square.\n\nThere can be leading and trailing spaces."
      case 1 => "The controls are simple:\n- Left-click a square to fill it\n- Right-click a square to mark it as empty\n\nIf you are unsure about the accuracy of your guesses,\nyou can use 'Draft Mode' to try filling squares.\n\nDraft mode gives the possibility of cancelling or validating\nchoices once you're ready to return to regular mode."
    }

    print(content, Vec(100, 500), BLACK)
  }
}
