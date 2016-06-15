package heigvd.nonograms

import java.util.Date

import com.github.dunnololda.scage.ScageLib._
import com.github.dunnololda.scage.support.{ScageColor, Vec}

import scalaj.http._

object selectedGrid {
  var grid: Grid = new Grid()
  var userGrid: UserGrid = new UserGrid(grid)

  def setGrid(g: Grid) = {
    grid = g
    userGrid = new UserGrid(grid)
  }
  def getGrid() = grid
  def getUserGrid() = userGrid
}

object User {
  private val default = "user" + scala.util.Random.nextInt(Integer.MAX_VALUE)
  var current: String = ""

  /** Get the user name or the default one is undefined */
  def getUser = {
    if (current.equals("")) {
      default
    } else {
      current
    }
  }
}

object Colors {
  val METRO_RED = new ScageColor("Metro Red", 0xd1, 0x11, 0x41)
  val METRO_GREEN = new ScageColor("Metro Green", 0x00, 0xb1, 0x59)
  val METRO_BLUE = new ScageColor("Metro Blue", 0x00, 0xae, 0xdb)
  val METRO_ORANGE = new ScageColor("Metro Orange", 0xf3, 0x77, 0x35)
  val METRO_YELLOW = new ScageColor("Metro Yellow", 0xff, 0xc4, 0x25)
}

object NonogramsOffline extends Screen("Nonograms") with MultiController {

  def g: Grid = selectedGrid.getGrid()
  def userGrid: UserGrid = selectedGrid.getUserGrid()

  val cal = java.util.Calendar.getInstance()

  // heartbeat towards elastic server.
  val uri = "http://search-elastic-search-heig-3nhbodzwhflo56pew23jotan6a.eu-central-1.es.amazonaws.com/nonogramsv01/stats/"
  // trigger to commit or not
  val commitResult = false
  // generate random indices
  def idx = scala.util.Random.nextInt(Integer.MAX_VALUE)

  // timer to trigger action every second
  val timer = Timer(5000) {
    val i = idx
    println("heartbeat: sending data with ID #" + i)

    // push the current result to server
    if (commitResult) {
      cal.setTimeInMillis(time_reference_to_use)
      val elapsed = time_string
      cal.setTimeInMillis(time_reference_to_use + userGrid.penaltiesTime)
      val penalty = time_string
      cal.setTimeInMillis(time_reference_to_use + userGrid.penaltiesTime)
      val total_time = time_string
      val result = Http(uri + i).postData("{" +
        "\"user\":\""+ User.getUser + "\"," + // string
        "\"version\":\"" + "0.1" + "\"," + // string
        "\"grid_id\":" + g.gridid + "\"," + // int
        "\"time\":\"" + System.currentTimeMillis() + "\"," + // int
        "\"time_elapsed\":\"" + elapsed + "\"," + // formatted date mm:ss:SSS
        "\"time_elapsed_raw\":" + time_reference_to_use + "," + // int
        "\"time_penalty\":\"" + penalty + "\"," + // formatted date mm:ss:SSS
        "\"time_penalty_raw\":" + userGrid.penaltiesTime + "," + // int
        "\"penalty_count\":" + userGrid.numberPenaltiesCache + ", " +
        "\"time_total\":\"" + total_time + "\"," + // formatted date mm:ss:SSS
        "\"time_total_raw\":" + (time_reference_to_use + userGrid.penaltiesTime) + "," + // int
        "\"filled_value\":" + userGrid.numberFilledCache + "," +
        "\"filled_percent\":" + filled_percent + "," + // int
        "\"finished\":" + userGrid.isfinished + "," + // bool
        "\"production\":" + false + "" + // bool
        "}").asString

      println(result.body)
    }

  }

  backgroundColor = WHITE

  val back_button = new Button(windowWidth - 110, 400, 100, 70, "Back", Colors.METRO_ORANGE, NonogramsOffline, () => {
    stop()
  })

  val reset_button = new Button(windowWidth - 110, 320, 100, 70, "Reset", Colors.METRO_RED, NonogramsOffline, () => {
    userGrid.resetGame()
  })

  val maybeButton : ToggleButton = new ToggleButton(10, 400, 200, 70,
    Colors.METRO_BLUE, GRAY, "To Draft Mode", "In Draft Mode", NonogramsOffline,
    () => {
      cancelButton.activate()
      validateButton.activate()
      maybeStatus = true
      println("Switching to draft mode")
    })
  val cancelButton : ToggleButton = new ToggleButton(10, 320, 95, 70,
    Colors.METRO_RED, WHITE, "Cancel\nDraft", "", NonogramsOffline,
    () => {
      userGrid.removeAllMaybe()
      maybeButton.activate()
      validateButton.deactivate()
      maybeStatus = false
      println("Cancelled draft")
    })
  val validateButton : ToggleButton = new ToggleButton(115, 320, 95, 70,
    Colors.METRO_GREEN, WHITE, "Apply\nDraft", "", NonogramsOffline,
    () => {
      userGrid.validateAllMaybe()
      maybeButton.activate()
      cancelButton.deactivate()
      maybeStatus = false
      println("Validated draft")
    })
  cancelButton.deactivate()
  validateButton.deactivate()

  var maybeStatus = false

  // val g = (new Grid)
  // g.printGrid()
  // g.printHints()

  // val userGrid = new UserGrid(g)
  userGrid.printMyGrid()
  def userSol = userGrid.userSolution

  def grid = g.solution
  def sizeX = g.sizeX
  def sizeY = g.sizeY
  def rowHintMax = g.rows_hint.map(_.size).max
  def colHintMax = g.cols_hint.map(_.size).max

  val gridSpacing = 21
  val gridOffset = 10
  val fullSize = 18

  // The origin X is at the left of the grid (row hints to the left, grid to the right)
  // The origin Y is at the bottom of the grid (grid and col hints are above)
  // Both are designed to center [grid+hints] in the middle of the window.
  def originX = (windowWidth - (gridSpacing * (sizeX - rowHintMax))) / 2
  def originY = (windowHeight - (gridSpacing * (sizeY + colHintMax))) / 2


  /* layout params for information about game status */
  def Xprint_data = originX
  def Xprint_text = originX + textOffset
  def Yprint_text (i:Int) = originY - i * fullSize
  val textOffset = gridSpacing * 5

  /* Return the cal as a formatted string. Update cal first. */
  def time_string = Time.current(cal)

  render {
    // horizontal grid lines
    val lenHorizontal = gridSpacing * sizeX
    val lenHints = gridSpacing * rowHintMax
    for (y <- 0 to sizeY) {
      val (_, pos) = arrayToScreen(0, y)
      val color = if (y == 0 || y == sizeY) BLACK else if (y%5 == 0) Colors.METRO_ORANGE else GRAY
      drawLine(Vec(originX - lenHints, pos), Vec(originX + lenHorizontal, pos), color)
    }

    // vertical grid lines
    val lenVertical = gridSpacing * (sizeY + colHintMax)
    for (x <- 0 to sizeX) {
      val (pos, _) = arrayToScreen((sizeX - x), 0)
      val color = if (x == 0 || x == sizeX) BLACK else if (x%5 == 0) Colors.METRO_ORANGE else GRAY
      drawLine(Vec(pos, originY), Vec(pos, originY + lenVertical), color)
    }

    // writing row hints
    for (row <- 0 until g.rows_hint.size; hint <- 0 until g.rows_hint(row).size) {
      val (posX, posY) = arrayToScreen(- (g.rows_hint(row).size - hint), row, gridOffset)
      print(g.rows_hint(row)(hint), Vec(posX, posY + 2), BLACK, align = "center")
    }
    // writing column hints
    for (col <- 0 until g.cols_hint.size; hint <- 0 until g.cols_hint(col).size) {
      val (posX, posY) = arrayToScreen(col, -(g.cols_hint(col).size - hint), gridOffset)
      print(g.cols_hint(col)(hint), Vec(posX, posY), BLACK, align = "center")
    }

    // filled squares for solution
    /*for (x <- 0 until sizeX; y <- 0 until sizeY) {
      if (grid(x)(y)) {
        val (posX,posY) = arrayToScreen(x,y,gridOffset)
        drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, BLACK)
      }
    }*/

    // filled squares for mygrid current game
    for (x <- 0 until sizeX; y <- 0 until sizeY) {
      val (posX,posY) = arrayToScreen(x,y,gridOffset)

      // to hide solution (maybe to remove)
      //drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, WHITE)

      userSol(x)(y) match {
        case Empty() =>
          print("X", Vec(posX, posY + 2), BLACK, align = "center")
        case Filled() =>
          drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, DARK_GRAY)
        case None() =>
          // does nothing
        case MaybeEmpty() =>
          print("?", Vec(posX, posY + 2), Colors.METRO_BLUE, align = "center")
        case MaybeFilled() =>
          drawFilledRectCentered(Vec(posX, posY), fullSize, fullSize, Colors.METRO_BLUE)
          print("?", Vec(posX, posY + 2), WHITE, align = "center")
        case Tried() =>
          print("X", Vec(posX, posY + 2), Colors.METRO_RED, align = "center")
      }
    }

    // information about current game status / evolution
    if (userGrid.isfinished) {
      print("CONGRATS!", Vec(Xprint_data, Yprint_text(5)), BLACK, "default")
    } else {
      print("...keep playing...", Vec(Xprint_data, Yprint_text(5)), BLACK, "default")
    }

    // current state of game
    val text = filled_percent + "% (" + userGrid.numberFilledCache + "/" + g.numberFilledCache + ")"
    print(text, Vec(Xprint_text, Yprint_text(1)), BLACK)

    // timers
    cal.setTimeInMillis(time_reference_to_use)
    print(time_string, Vec(Xprint_text, Yprint_text(2)))

    // show the penalties in red if any
    if (userGrid.numberPenaltiesCache > 0) {
      cal.setTimeInMillis(userGrid.penaltiesTime)
      print(time_string, Vec(Xprint_text, Yprint_text(3)), Colors.METRO_RED, "default")
      cal.setTimeInMillis(time_reference_to_use + userGrid.penaltiesTime)
      print(time_string, Vec(Xprint_text, Yprint_text(4)), Colors.METRO_RED, "default")
    } else {
      // otherwise, just print 0 time for penalties
      print(time_string, Vec(Xprint_text, Yprint_text(4)), BLACK, "default")
      cal.setTimeInMillis(0)
      print(time_string, Vec(Xprint_text, Yprint_text(3)), BLACK, "default")
    }


  }

  def filled_percent = {
    if (g.numberFilledCache != 0) {
      userGrid.numberFilledCache * 100 / g.numberFilledCache
    } else {
      -1
    }
  }

  def time_reference_to_use = {
    if (userGrid.isfinished) {
      userGrid.time_finished
    } else {
      userGrid.time_elapsed
    }
  }

  interface {
    print(xml("launcher.info"), 10, 10, 10.0f, BLACK)

    // information about current game status / evolution
    print("Playing:", Vec(Xprint_data, Yprint_text(1)), BLACK)
    print("Time:", Vec(Xprint_data, Yprint_text(2)), BLACK)
    print("Penalties:", Vec(Xprint_data, Yprint_text(3)), BLACK)
    drawLine(Vec(Xprint_data, Yprint_text(3) - 2), Vec(Xprint_data + gridSpacing * 10, Yprint_text(3) - 2), BLACK)
    print("Total time:", Vec(Xprint_data, Yprint_text(4)), BLACK)
    val useless = g.numberFilled()
  }

  /* Handles left-click events */
  leftMouse(onBtnDown = {m =>
    //maybeButton.click(m)
    //cancelButton.click(m)
    //validateButton.click(m)

    // if the game is finished, has no effect
    if (userGrid.isfinished) {
      println("no clicks: game is finished")
    } else {
      val (x, y) = screenToArray(m.x, m.y)

      // checks that the click is within the array
      if (!((x >= 0) && (x < sizeX) && (y >= 0) && (y < sizeY))) {
        println("no clicks here")
      } else {
        if (maybeStatus) {
          userSol(x)(y) = userSol(x)(y) match {
            case None() => MaybeFilled()
            case MaybeEmpty() => None()
            case MaybeFilled() => None()
            case _ => userSol(x)(y)
          }
        } else {
          val valid = !userGrid.checkMode || (userGrid.checkMode && grid(x)(y))
          println(valid)
          userSol(x)(y) = userSol(x)(y) match {
            case None() => if (valid) Filled() else Tried()
            case Empty() => None()
            case Filled() => None()
            case _ => userSol(x)(y)
          }

          userGrid.checkGameFinished(userGrid.checkMode)
        }

      }
    }
  })

  /* Handles right-click events */
  rightMouse(onBtnDown = { m =>
    // if the game is finished, has no effect
    if (userGrid.isfinished) {
      println("no clicks: game is finished")
    } else {
      val (x, y) = screenToArray(m.x, m.y)

      // checks that the click is within the array
      if (!((x >= 0) && (x < sizeX) && (y >= 0) && (y < sizeY))) {
        println("no clicks here")
      } else {
        if (maybeStatus) {
          userSol(x)(y) = userSol(x)(y) match {
            case None() => MaybeEmpty()
            case MaybeEmpty() => None()
            case MaybeFilled() => None()
            case _ => userSol(x)(y)
          }
        } else {
          userSol(x)(y) = userSol(x)(y) match {
            case None() => Empty()
            case Empty() => None()
            case Filled() => None()
            case _ => userSol(x)(y)
          }
        }
      }
    }
  })

  /* Helper method to convert array position to screen positions */
  def arrayToScreen(x: Int, y: Int, offset: Int = 0): (Int, Int) = {
    (
      originX + offset + (x * (gridSpacing)),
      originY - offset + ((sizeY - y) * (gridSpacing))
      )
  }

  /* Helper method to convert screen position to array positions.
  Be careful, it may be OUTSIDE the array, and cause IndexOutOfBoundsException) */
  def screenToArray(xs: Double, ys: Double, offset: Int = 0): (Int, Int) = {
    (
      ((xs - originX - offset) / gridSpacing).floor.toInt,
      (sizeY - ((ys - originY + offset) / gridSpacing)).floor.toInt
      )
  }
}

// Timer ideas from http://otfried.org/scala/timers.html
object Time {
  private val form = new java.text.SimpleDateFormat("mm:ss:SSS")
  def current (time:java.util.Calendar = java.util.Calendar.getInstance()) = form.format(time.getTime)
}

object Timer {
  def apply(interval: Int, repeats: Boolean = true)(op: => Unit) {
    val timeOut = new javax.swing.AbstractAction() {
      def actionPerformed(e : java.awt.event.ActionEvent) = op
    }
    val t = new javax.swing.Timer(interval, timeOut)
    t.setRepeats(repeats)
    t.start()
  }
}
