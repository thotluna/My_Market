package ve.com.teeac.mymarket.presentation.marketdetails

import ve.com.teeac.mymarket.domain.model.MarketDetail

data class DetailsMarketState(
    val marketId: Long? = null,
    val list: List<MarketDetail> = emptyList(),
)
