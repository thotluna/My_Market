package ve.com.teeac.mymarket.presentation.markets

import ve.com.teeac.mymarket.domain.model.Market

data class MarketsState(
    val markets: List<Market> = emptyList(),
)
