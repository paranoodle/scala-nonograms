package heigvd.nonograms.models

/**
  * Managed the current user in the current game.
  */
object User {
  lazy val userID = "user" + scala.util.Random.nextInt(Integer.MAX_VALUE)
  var current: String = ""

  /** Get the user name or the default one is undefined */
  def getUser = {
    if (current.equals("")) {
      userID
    } else {
      current
    }
  }
}
