package heigvd.nonograms.utils

/**
  * Manage time formats and timer.
  */

// Timer ideas from http://otfried.org/scala/timers.html

object Time {
  private val form = new java.text.SimpleDateFormat("mm:ss:SSS")
  def current (time:java.util.Calendar = java.util.Calendar.getInstance()) = form.format(time.getTime)
}

object DateTime {
  private val form = new java.text.SimpleDateFormat("YYYY-MM-YY HH:mm")
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


