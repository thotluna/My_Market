package ve.com.teeac.mymarket.data

import ve.com.teeac.mymarket.domain.model.Market
import javax.inject.Inject

class MarketRepository @Inject constructor() {
    fun getMarkets() = (1L..50L).map {
        val converter: Double = 5.2
        val calAmountDollar: Double = 2.0 * it.toDouble()
        Market(
            id = it,
            date = System.currentTimeMillis()-(86400000 * it.toInt()),
            amountDollar = calAmountDollar,
            amount = calAmountDollar * converter
        )
    }
}