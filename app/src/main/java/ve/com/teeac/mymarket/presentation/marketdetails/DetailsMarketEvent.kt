package ve.com.teeac.mymarket.presentation.marketdetails

sealed class DetailsMarketEvent{
    object SaveAmountSetup : DetailsMarketEvent()
    object SaveProduct: DetailsMarketEvent()
    object ClearProductForm: DetailsMarketEvent()
    data class UpdateProduct( val idProduct: Long): DetailsMarketEvent()
    data class DeleteProduct( val idProduct: Long): DetailsMarketEvent()
}


