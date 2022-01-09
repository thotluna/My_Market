package ve.com.teeac.mymarket.data
import ve.com.teeac.mymarket.domain.model.MarketDetail

class MarketDetailRepository {

    fun getDetail()= (1..5).map {
        val converter: Double = 5.2
        val quantity = it/3.00
        MarketDetail(
            marketId = 1,
            id = it,
            quantity = quantity,
            description="Product number $it",
            unitAmountDollar = it * 0.42,
            unitAmount = (it * 0.42) * converter,
            amountDollar = (it * 0.42) * quantity,
            amount = (it * 0.42) * 5.2 * quantity
        )
    }
}