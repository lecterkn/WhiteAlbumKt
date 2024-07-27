package me.lecter.whitealbum.client

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.security.SecureRandom
import java.util.*

class RiotAPI {

    companion object {
        fun tokenUrlSafe(numBytes: Int): String {
            val random = SecureRandom()
            val bytes = ByteArray(numBytes)
            random.nextBytes(bytes)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
        }
    }

    var USER_AGENT: String = "RiotClient/90.0.2.1805.3774 %s (Windows;10;;Professional, x64)"
    var RIOTCLIENT_VERSION: String = "release-08.07-shipping-9-2444158"
    val RIOTCLIENT_PLATFORM: String = "ew0KCSJwbGF0Zm9ybVR5cGUiOiAiUEMiLA0KCSJwbGF0Zm9ybU9TIjogIldpbmRvd3MiLA0KCSJwbGF0Zm9ybU9TVmVyc2lvbiI6ICIxMC4wLjE5MDQyLjEuMjU2LjY0Yml0IiwNCgkicGxhdGZvcm1DaGlwc2V0IjogIlVua25vd24iDQp9"

    init {
        updateVersion()
        refreshUserAgent()
    }

    fun updateVersion() {
        try {
            HttpClientBuilder.create().build().use { httpClient ->
                val httpGet: HttpGet = HttpGet("https://valorant-api.com/v1/version")
                httpGet.addHeader("accept", "application/json")
                httpClient.execute(httpGet).use { httpResponse ->
                    if (httpResponse.statusLine.statusCode == 200) {
                        val json = Json.parseToJsonElement(EntityUtils.toString(httpResponse.entity)).jsonObject
                        val version = json["data"]?.jsonObject?.get("riotClientBuild")?.jsonPrimitive?.content!!
                        RIOTCLIENT_VERSION =
                            json["data"]?.jsonObject?.get("riotClientVersion")?.jsonPrimitive?.content!!
                        println("RiotClient-Version: \"$RIOTCLIENT_VERSION\"")
                    } else {
                        throw IOException("failed to get data")
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun refreshUserAgent() {
        USER_AGENT = tokenUrlSafe(96).replace("_", "-")
    }
}
