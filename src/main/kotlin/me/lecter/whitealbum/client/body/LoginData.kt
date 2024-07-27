package me.lecter.whitealbum.client.body

import me.lecter.whitealbum.client.valorant.items.ItemOffer
import me.lecter.whitealbum.client.valorant.items.NightMarketOffer

data class LoginData(
    val username: String,
    val singleItemOffers: MutableList<ItemOffer>,
    val nightMarket: MutableList<NightMarketOffer>,
)
