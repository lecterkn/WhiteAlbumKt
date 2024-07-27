package me.lecter.whitealbum.server.http.settings

import me.lecter.whitealbum.config.ApplicationConfig

class SettingResponseBody(
    val message: String,
    val settings: ApplicationConfig?
) {
}