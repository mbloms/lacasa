/**
 * Copyright (C) 2015-2016 Philipp Haller
 */
package lacasa

object Util {

  val logEnabled = java.lang.System.getProperty("lacasa.plugin.logging", "false") == "true"

  def log(msg: => String): Unit = {
    if (logEnabled)
      println(msg)
  }

}
