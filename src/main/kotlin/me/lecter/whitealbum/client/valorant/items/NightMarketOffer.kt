package me.lecter.whitealbum.client.valorant.items

class NightMarketOffer(
    override var offerId: String,
    var originalCost: Int,
    override var displayName: String,
    override var displayIcon: String?,
    var discountPercent: Int,
    var finalCost: Int,
) : ItemOffer(offerId, originalCost, displayName, displayIcon)
