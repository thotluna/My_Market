package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import ve.com.teeac.mymarket.domain.model.MarketDetail

sealed class ProductEvent{
    data class EnteredQuantity(val value: Number?): ProductEvent()
    data class EnteredDescription(val value: String?): ProductEvent()
    data class EnteredAmountBs(val value: Number?): ProductEvent()
    data class EnteredAmountDollar(val value: Number?): ProductEvent()

    data class UpdateProduct(val product: MarketDetail): ProductEvent()
    data class UpdateRate (val rate: Number): ProductEvent()
}