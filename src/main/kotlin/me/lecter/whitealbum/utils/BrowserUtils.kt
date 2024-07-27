package me.lecter.whitealbum.utils

import java.awt.Desktop
import java.net.URI

class BrowserUtils {
    companion object {
        fun open(url: String): Boolean {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI(url))
                return true
            }
            if (OSUtils.OS == OSType.WINDOWS) {
                Runtime.getRuntime().exec("cmd /c start $url")
            } else {
                Runtime.getRuntime().exec("open $url")
            }
            return false
        }
    }
}
