package ve.com.teeac.mymarket.presentation.markets

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
    data class NavigateToDetails(val marketId: Long?): UiEvent()
}