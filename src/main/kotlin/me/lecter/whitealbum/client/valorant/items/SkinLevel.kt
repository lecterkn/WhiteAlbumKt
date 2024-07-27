package me.lecter.whitealbum.client.valorant.items

import kotlinx.serialization.Serializable

@Serializable
data class SkinLevel(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
    val assetPath: String,
)
