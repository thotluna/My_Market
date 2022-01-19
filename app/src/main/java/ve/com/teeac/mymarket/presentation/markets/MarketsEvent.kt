package ve.com.teeac.mymarket.presentation.markets

sealed class MarketsEvent {
    data class NavigateMarket(val idMarket: Long? = null): MarketsEvent()
}