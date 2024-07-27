package me.lecter.whitealbum.client.body

import kotlinx.serialization.Serializable
import me.lecter.whitealbum.client.RiotAPI.Companion.tokenUrlSafe
import java.security.SecureRandom
import java.util.*

@Serializable
data class AuthBody(
    val acr_values: String = "",
    val claims: String = "",
    val client_id: String = "riot-client",
    val code_challenge: String = "",
    val code_challenge_method: String = "",
    val nonce: String = tokenUrlSafe(16),
    val redirect_uri: String = "http://localhost/redirect",
    val response_type: String = "token id_token",
    val scope: String = "openid link ban lol_region account",
) {
    companion object {
        fun getAuthBody(): AuthBody {
            return AuthBody(client_id = "play-valorant-web-prod", nonce = "1", redirect_uri = "https://playvalorant.com/opt_in", scope = "account openid")
        }
    }
}

