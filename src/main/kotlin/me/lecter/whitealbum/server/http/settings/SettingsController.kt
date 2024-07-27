package me.lecter.whitealbum.server.http.settings

import me.lecter.whitealbum.client.enum.APILanguage
import me.lecter.whitealbum.client.enum.Region
import me.lecter.whitealbum.config.ApplicationConfig
import me.lecter.whitealbum.server.service.settings.SettingService
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class SettingsController(
    val settingService: SettingService
) {

    @RequestMapping(value=["/settings"], method = [RequestMethod.GET])
    fun get() : ApplicationConfig {
        return settingService.getSettings()
    }

    @RequestMapping(value=["/settings"], method = [RequestMethod.PATCH])
    fun patch(@RequestBody configs: ApplicationConfig): ResponseEntity<SettingResponseBody> {
        settingService.setSettings(configs)
        return ResponseEntity.ok(SettingResponseBody("success", settingService.getSettings()))
    }

    @RequestMapping(value=["/settings/region"], method = [RequestMethod.GET])
    fun regions() : List<Region> {
        return Region.entries.toList()
    }

    @RequestMapping(value=["/settings/language"], method = [RequestMethod.GET])
    fun languages() : List<APILanguage> {
        return APILanguage.entries.toList()
    }
}