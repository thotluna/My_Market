package ve.com.teeac.mymarket.presentation

class InvalidEventException(message: String):Exception(message){
    companion object{
        const val DETAILS_MARKET_EVENT = "Do not exist more event in details market"
        const val PRODUCT_EVENT = "Do not exist more event in product form"
    }
}