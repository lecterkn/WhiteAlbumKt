package me.lecter.whitealbum.client

import kotlinx.serialization.json.*
import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.client.RiotClient.Companion.api
import me.lecter.whitealbum.client.valorant.StoreFront
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

class ValorantAPI(
    private val access_token: String,
    private val entitlements_token: String,
) {
    private var user_id: String = "null"
    private var gameName: String = "null"

    init {
        setPlayerID()
    }

    fun setPlayerID() {
        val payloads = access_token.split(".")
        val decoded = decodeBase64URLSafe(payloads[1])
        val payload = Json.parseToJsonElement(decoded).jsonObject

        user_id = payload["sub"]?.jsonPrimitive?.content!!
        println("user_id: \"$user_id\"")

        try {
            HttpClientBuilder.create().build().use { httpClient ->
                val httpPut = HttpPut("https://pd.${WhiteAlbum.configuration.region.value}.a.pvp.net/name-service/v2/players")
                setDefaultHeaders(httpPut)
                httpPut.entity = StringEntity("[\"$user_id\"]", "UTF-8")
                httpClient.execute(httpPut).use { httpResponse ->
                    if (httpResponse.statusLine.statusCode == 200) {
                        val responseJson = Json.parseToJsonElement(EntityUtils.toString(httpResponse.entity, "UTF-8")).jsonArray
                        val data = responseJson[0].jsonObject
                        gameName = "${data["GameName"]?.jsonPrimitive?.content!!}#${data["TagLine"]?.jsonPrimitive?.content!!}"
                    } else {
                        println("failed to get player data \"${httpResponse.statusLine.statusCode}\"")
                        println(EntityUtils.toString(httpResponse.entity, "UTF-8"))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getStoreFront(): StoreFront? {
        try {
            HttpClientBuilder.create().build().use { httpClient ->
                val httpGet = HttpGet("https://pd.${WhiteAlbum.configuration.region.value}.a.pvp.net/store/v2/storefront/$user_id")
                setDefaultHeaders(httpGet)
                httpClient.execute(httpGet).use { httpResponse ->
                    if (httpResponse.statusLine.statusCode == 200) {
                        val responseBody = EntityUtils.toString(httpResponse.entity, "UTF-8")
                        return StoreFront(gameName, responseBody)
                    } else {
                        println("failed to get storefront \"${httpResponse.statusLine.statusCode}\"")
                        println(EntityUtils.toString(httpResponse.entity, "UTF-8"))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun setDefaultHeaders(httpRequest: HttpRequestBase) {
        httpRequest.addHeader("User-Agent", api.USER_AGENT)
        httpRequest.addHeader("Accept", "application/json")
        httpRequest.addHeader("Authorization", "Bearer $access_token")
        httpRequest.addHeader("X-Riot-Entitlements-JWT", entitlements_token)
        httpRequest.addHeader("X-Riot-ClientPlatform", RiotClient.api.RIOTCLIENT_PLATFORM)
        httpRequest.addHeader("X-Riot-ClientVersion", RiotClient.api.RIOTCLIENT_VERSION)
        httpRequest.addHeader("Content-Type", "application/json")
    }

    private fun decodeBase64URLSafe(encodedString: String): String {
        // Base64クラスのURLセーフなデコーダーを取得
        val decoder = Base64.getUrlDecoder()

        // デコード
        val decodedBytes = decoder.decode(encodedString)

        // バイト配列を文字列に変換
        return String(decodedBytes, StandardCharsets.UTF_8)
    }
}
