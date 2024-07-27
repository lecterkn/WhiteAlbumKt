package me.lecter.whitealbum

import me.lecter.whitealbum.account.AccountManager
import me.lecter.whitealbum.client.valorant.StoreFront
import me.lecter.whitealbum.client.valorant.items.SkinLevel
import me.lecter.whitealbum.config.ApplicationConfig
import me.lecter.whitealbum.config.ConfigManager
import me.lecter.whitealbum.utils.BrowserUtils
import java.security.Security

class WhiteAlbum {
    companion object {
        const val NAME: String = "WhiteAlbum"
        const val VERSION: String = "2.0.1"
        const val DEVELOPER: String = "github@lecterkn"
        val accountManager: AccountManager = AccountManager()
        var configuration: ApplicationConfig = ApplicationConfig()

        fun pre() {
            println("=== INFOMATION ===")
            println("$NAME $VERSION")
            println(DEVELOPER)
            println("==================")

            configuration = ConfigManager.load()
            accountManager.load()

            if (configuration.skinLevels_preload) {
                if (StoreFront.setSkinLevels()) {
                    println("skinLevels preloaded.")
                }
                else {
                    println("failed to load skinLevels.")
                }
            }

            Runtime.getRuntime().addShutdownHook(
                Thread {
                    ConfigManager.save()
                },
            )
        }

        fun post() {
            BrowserUtils.open("http://localhost:8100/lecterkn/")
        }
    }
}
