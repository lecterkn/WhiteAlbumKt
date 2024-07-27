package me.lecter.whitealbum.config

import kotlinx.serialization.Serializable
import me.lecter.whitealbum.client.enum.APILanguage
import me.lecter.whitealbum.client.enum.Region

@Serializable
data class ApplicationConfig(
    val skinLevels_preload: Boolean = true,
    val region: Region = Region.AP,
    val language: APILanguage = APILanguage.JP,
)
