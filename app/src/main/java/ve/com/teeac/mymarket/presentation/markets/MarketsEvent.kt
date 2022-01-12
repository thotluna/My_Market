package ve.com.teeac.mymarket.presentation.markets

sealed class MarketsEvent {
    object SaveMarket: MarketsEvent()
    data class NavigateMarket(val idMarket: Long? = null): MarketsEvent()
}