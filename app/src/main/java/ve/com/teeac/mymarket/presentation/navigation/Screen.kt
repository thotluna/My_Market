package ve.com.teeac.mymarket.presentation.navigation

sealed class Screen(val route: String){

    object MarketsScreen: Screen("markets_screen")

    object DetailsMarketScreen: Screen("details_market_screen"){
        fun createRoute(marketId: Long = -1L): String {
            return route +"?marketId=${marketId}"
        }
    }
}
