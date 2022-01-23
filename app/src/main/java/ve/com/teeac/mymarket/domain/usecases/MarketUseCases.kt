package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.usecases.market_use_cases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.DeleteMarket
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.GetMarkets

data class MarketUseCases(
    val addMarket: AddMarket,
    val getMarkets: GetMarkets,
    val deleteMarket: DeleteMarket
)