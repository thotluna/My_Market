package ve.com.teeac.mymarket.presentation.marketdetails.product_form

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
}