package me.lecter.whitealbum.client.valorant

import kotlinx.serialization.json.*
import me.lecter.whitealbum.WhiteAlbum
import me.lecter.whitealbum.client.valorant.items.ItemOffer
import me.lecter.whitealbum.client.valorant.items.NightMarketOffer
import me.lecter.whitealbum.client.valorant.items.SkinLevel
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import java.io.IOException

class StoreFront(
    val username: String,
    val json: String,
) {
    companion object {
        private var skinLevels: List<SkinLevel> = arrayListOf()

        fun setSkinLevels(): Boolean {
            try {
                HttpClientBuilder.create().build().use { httpClient ->
                    val httpGet =
                        HttpGet("https://valorant-api.com/v1/weapons/skinlevels/?language=${WhiteAlbum.configuration.language.value}")
                    httpClient.execute(httpGet).use { httpResponse ->
                        if (httpResponse.statusLine.statusCode == 200) {
                            val responseBody = EntityUtils.toString(httpResponse.entity)
                            skinLevels = Json.decodeFromJsonElement(Json.parseToJsonElement(responseBody).jsonObject["data"]?.jsonArray!!)
                            return true
                        } else {
                            println("failed to get skinlevels \"${httpResponse.statusLine.statusCode}\"")
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return false
        }

        fun getSkinLevel(uuid: String): SkinLevel? {
            for (skinLevel in skinLevels) {
                if (skinLevel.uuid == uuid) {
                    return skinLevel
                }
            }
            return null
        }
    }

    var singleItemOffers: MutableList<ItemOffer>? = null
    var nightMarket: MutableList<NightMarketOffer>? = null

    init {
        singleItemOffers = mutableListOf<ItemOffer>()
        nightMarket = mutableListOf<NightMarketOffer>()
        if (skinLevels.isEmpty()) {
            setSkinLevels()
        }
        val storeFrontJson = Json.parseToJsonElement(json).jsonObject

        println(json)

        // singleItemOffers設定
        for (element in storeFrontJson["SkinsPanelLayout"]?.jsonObject?.get("SingleItemStoreOffers")?.jsonArray!!) {
            val offerId = element.jsonObject["OfferID"]?.jsonPrimitive?.content!!
            val skinLevel = getSkinLevel(offerId)
            if (skinLevel != null) {
                val itemOffer =
                    ItemOffer(
                        offerId,
                        element.jsonObject["Cost"]?.jsonObject?.get("85ad13f7-3d1b-5128-9eb2-7cd8ee0b5741")?.jsonPrimitive?.int!!,
                        skinLevel.displayName,
                        skinLevel.displayIcon,
                    )
                singleItemOffers!!.add(itemOffer)
            }
        }

        // nightMarket設定
        if (storeFrontJson.containsKey("BonusStore")) {
            for (element in storeFrontJson["BonusStore"]?.jsonObject?.get("BonusStoreOffers")?.jsonArray!!) {
                val offerId = element.jsonObject["Offer"]?.jsonObject?.get("OfferID")?.jsonPrimitive?.content!!
                val skinLevel = getSkinLevel(offerId)
                if (skinLevel != null) {
                    val itemOffer =
                        NightMarketOffer(
                            offerId,
                            element.jsonObject["Offer"]?.jsonObject?.get(
                                "Cost",
                            )?.jsonObject?.get("85ad13f7-3d1b-5128-9eb2-7cd8ee0b5741")?.jsonPrimitive?.int!!,
                            skinLevel.displayName,
                            skinLevel.displayIcon,
                            element.jsonObject["DiscountPercent"]?.jsonPrimitive?.int!!,
                            element.jsonObject["DiscountCosts"]?.jsonObject?.get(
                                "85ad13f7-3d1b-5128-9eb2-7cd8ee0b5741",
                            )?.jsonPrimitive?.int!!,
                        )
                    nightMarket!!.add(itemOffer)
                }
            }
        }
    }
}
