package ve.com.teeac.mymarket.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ve.com.teeac.mymarket.presentation.marketdetails.DetailsMarketScreen
import ve.com.teeac.mymarket.presentation.markets.MarketsScreen


fun NavGraphBuilder.marketsAppGraph(navController: NavHostController) {
    composable(route = Screen.MarketsScreen.route) {
        MarketsScreen(
             onClick = { marketId ->
                navController.navigate(Screen.DetailsMarketScreen.createRoute(marketId!!))
            }
        )
    }
    composable(
        route = Screen.DetailsMarketScreen.route+"?marketId={marketId}",
        arguments = listOf(
            navArgument(
                name = "marketId"
            ){
                type = NavType.LongType
                defaultValue = 0L
            }
        )
    ){
        DetailsMarketScreen (
            navigateUp = { navController.navigateUp() }
        )
    }
}