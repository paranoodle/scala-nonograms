package heigvd.nonograms.models

/**
  * Managed the current user in the current game.
  */
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
