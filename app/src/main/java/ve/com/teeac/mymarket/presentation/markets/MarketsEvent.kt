package ve.com.teeac.mymarket.presentation.markets

sealed class MarketsEvent {
    object SaveMarket: MarketsEvent()
}