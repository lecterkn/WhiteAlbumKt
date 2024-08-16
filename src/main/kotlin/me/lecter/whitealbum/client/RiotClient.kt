package me.lecter.whitealbum.client

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import me.lecter.whitealbum.client.body.AuthBody
import me.lecter.whitealbum.client.body.LoginBody
import me.lecter.whitealbum.client.exceptions.RiotAuthenticationException
import me.lecter.whitealbum.client.exceptions.RiotRateLimitException
import me.lecter.whitealbum.client.exceptions.RiotUnknownException
import me.lecter.whitealbum.proxy.ProxyManager
import org.apache.http.HttpHost
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.util.regex.Pattern
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

class RiotClient(
    private val username: String,
    private val password: String,
) {
    companion object {
        val api: RiotAPI = RiotAPI()
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val ENCODING: String = "UTF-8"

        private fun setDefaultHeaders(httpRequest: HttpRequestBase) {
            httpRequest.addHeader("X-Curl-Source", "Api")
            httpRequest.addHeader("Accept-Encoding", "deflate, gzip, zstd")
            httpRequest.addHeader("User-Agent", api.USER_AGENT)
            httpRequest.addHeader("Cache-Control", "no-cache")
            httpRequest.addHeader("Accept", "application/json")
            httpRequest.addHeader("Referer", "https://auth.riotgames.com/")
        }

        fun getSSLSocketFactory(): SSLConnectionSocketFactory {
            return SSLConnectionSocketFactory(
                SSLContext.getDefault(),
                //arrayOf("TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3"),
                arrayOf("TLSv1.3"),
                arrayOf("TLS_CHACHA20_POLY1305_SHA256", "TLS_AES_128_GCM_SHA256", "TLS_AES_256_GCM_SHA384"),
                DefaultHostnameVerifier(),
            )
        }
    }

    var access_token: String? = null
    var entitlements_token: String? = null
    var logged: Boolean = false

    fun login(): Boolean {
        api.refreshUserAgent()
        try {
            val cookieStore = BasicCookieStore()
            val bulder = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .setSSLSocketFactory(getSSLSocketFactory())
                .setDefaultCookieStore(cookieStore)
            /*
            if (ProxyManager.proxyList.isEmpty().not()) {
                val proxyServer = ProxyManager.getNext()
                bulder.setProxy(HttpHost(proxyServer.ipAddress, proxyServer.port))
            }
             */
                bulder.build().use { httpClient ->
                    val json = Json { encodeDefaults = true }

                    val httpPost = HttpPost("https://auth.riotgames.com/api/v1/authorization")
                    val authBody = json.encodeToString(AuthBody()) // リクエストのボディ
                    setDefaultHeaders(httpPost)
                    httpPost.entity = StringEntity(authBody, ENCODING)
                    httpPost.addHeader("Content-Type", "application/json")

                    // POSTリクエストを送信
                    httpClient.execute(httpPost).use { httpResponse ->
                        logger.info(EntityUtils.toString(httpResponse.entity))
                        if (httpResponse.statusLine.statusCode == 200) {
                            println("post success")
                        }
                    }

                    val httpPut = HttpPut("https://auth.riotgames.com/api/v1/authorization")
                    val loginBody: LoginBody = LoginBody(username = username, password = password)
                    val loginJson = json.encodeToString(loginBody)
                    setDefaultHeaders(httpPut)
                    httpPut.entity = StringEntity(loginJson, ENCODING)
                    httpPut.addHeader("Content-Type", "application/json")

                    // PUTリクエストを送信
                    httpClient.execute(httpPut).use { httpResponse ->
                        val body = EntityUtils.toString(httpResponse.entity)
                        if (httpResponse.statusLine.statusCode != 200) {
                            println("httpStatus: ${httpResponse.statusLine.statusCode}")
                            println(body)
                        }
                        val responseJson = Json.parseToJsonElement(body).jsonObject
                        val type = responseJson["type"]?.jsonPrimitive?.content!!
                        when (type) {
                            "auth" -> {
                                val error = responseJson["error"]?.jsonPrimitive?.content!!
                                println(body)
                                when (error) {
                                    "auth_failure" -> throw RiotAuthenticationException()
                                    "rate_limited" -> throw RiotRateLimitException()
                                    else -> throw RiotUnknownException()
                                }
                            }
                            "response" -> {
                                val uri =
                                    responseJson["response"]?.jsonObject?.get(
                                        "parameters",
                                    )?.jsonObject?.get("uri")?.jsonPrimitive?.content!!
                                val pattern = Pattern.compile("access_token=(.+)&scope=")
                                val matcher = pattern.matcher(uri)
                                if (matcher.find()) {
                                    access_token = matcher.group(1)
                                    println("riot account logged in.")
                                } else {
                                    println("access_token does not found.")
                                    return false
                                }
                            }
                        }
                    }

                    val httpPost_entitlements = HttpPost("https://entitlements.auth.riotgames.com/api/token/v1")
                    setDefaultHeaders(httpPost_entitlements)
                    httpPost_entitlements.addHeader("Authorization", "Bearer $access_token")
                    httpPost_entitlements.addHeader("Content-Type", "application/json")
                    httpPost_entitlements.entity = StringEntity("{}", ENCODING)

                    // POSTリクエストを送信
                    httpClient.execute(httpPost_entitlements).use { httpResponse ->
                        if (httpResponse.statusLine.statusCode == 200) {
                            val responseBody = EntityUtils.toString(httpResponse.entity)
                            val responseJson = Json.parseToJsonElement(responseBody).jsonObject
                            entitlements_token = responseJson["entitlements_token"]?.jsonPrimitive?.content!!
                            println("success to get entitlements_token")
                            logged = true
                            return true
                        }
                    }
                }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return false
    }

}
