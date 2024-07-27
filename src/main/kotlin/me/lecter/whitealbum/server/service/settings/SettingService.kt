package me.lecter.whitealbum.server.service.settings

import me.lecter.whitealbum.config.ApplicationConfig
import org.springframework.stereotype.Service

@Service
interface SettingService {

    fun getSettings(): ApplicationConfig

    fun setSettings(configuration: ApplicationConfig)
}