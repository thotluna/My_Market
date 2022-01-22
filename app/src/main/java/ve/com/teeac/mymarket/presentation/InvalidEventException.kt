package ve.com.teeac.mymarket.presentation

class InvalidEventException(message: String):Exception(message){
    companion object{
        const val DETAILS_MARKET_EVENT = "Do not exist more event in details market"
    }
}

class InvalidPropertyApp(message: String): Exception(message){
    companion object{
        const val PRODUCT_DOES_NOT_EXIST = " Product does not exist"
        const val MARKET_DOES_NOT_EXIST = " Market does not exist"
        const val AMOUNTS_SETUP_DOES_NOT_EXIST = " Amount setup does not exist"
    }
}