package me.lecter.whitealbum.account

import kotlinx.serialization.Serializable

@Serializable
data class RiotAccount(
    val username: String,
    var password: String,
)
