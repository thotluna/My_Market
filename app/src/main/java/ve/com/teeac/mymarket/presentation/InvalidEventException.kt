package ve.com.teeac.mymarket.presentation

class InvalidEventException(message: String):Exception(message){
    companion object{
        const val DETAILS_MARKET_EVENT = "Do not exist more event in details market"
    }
}

class InvalidPropertyApp(message: String): Exception(message){
    companion object{
        const val PRODUCT_DO_NOT_EXIST = " Product does not exist"
    }
}