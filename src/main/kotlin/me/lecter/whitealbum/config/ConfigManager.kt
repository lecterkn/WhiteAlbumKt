package me.lecter.whitealbum.config

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.lecter.whitealbum.WhiteAlbum
import java.io.File
import java.io.FileWriter

class ConfigManager {
    companion object {
        private const val FILE_NAME = "config.json"

        fun load(): ApplicationConfig {
            val file = File(FILE_NAME)
            if (!file.exists()) {
                println("\"config.json\" does not exists.")
                save()
                return ApplicationConfig()
            }
            val body = file.readText()
            if (body.isEmpty()) {
                println("failed to load config.json")
                return ApplicationConfig()
            }
            println("loaded config.json")
            return Json.decodeFromString(body)
        }

        fun save(): Boolean {
            val json =
                Json {
                    encodeDefaults = true
                    prettyPrint = true
                }
            val body = json.encodeToString(WhiteAlbum.configuration)
            FileWriter(FILE_NAME).use { writer ->
                writer.write(body)
            }
            println("saved config.json")
            return true
        }
    }
}
