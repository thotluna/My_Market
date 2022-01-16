package ve.com.teeac.mymarket.presentation.marketdetails.product_form

sealed class ProductEvent{
    data class EnteredQuantity(val value: Number?): ProductEvent()
    data class EnteredDescription(val value: String?): ProductEvent()
    data class EnteredAmountBs(val value: Number?): ProductEvent()
    data class EnteredAmountDollar(val value: Number?): ProductEvent()

    data class LoadProduct(val id: Long): ProductEvent()
    data class Save(val idMarket: Long): ProductEvent()
    data class UpdateRate (val rate: Number?): ProductEvent()
}