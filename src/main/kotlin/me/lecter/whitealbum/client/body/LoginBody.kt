package me.lecter.whitealbum.client.body

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val language: String = "en_US",
    val password: String,
    val region: String? = null,
    val remember: Boolean = false,
    val type: String = "auth",
    val username: String,
)
