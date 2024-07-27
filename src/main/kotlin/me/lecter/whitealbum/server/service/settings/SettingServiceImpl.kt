package me.lecter.whitealbum.server.service.settings

import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.config.ApplicationConfig

class SettingServiceImpl: SettingService {

    override fun getSettings(): ApplicationConfig {
        return WhiteAlbum.configuration
    }

    override fun setSettings(configuration: ApplicationConfig) {
        WhiteAlbum.configuration = configuration
    }
}