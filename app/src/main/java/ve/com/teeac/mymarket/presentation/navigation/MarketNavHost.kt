package ve.com.teeac.mymarket.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ve.com.teeac.mymarket.presentation.marketdetails.DetailsMarketScreen
import ve.com.teeac.mymarket.presentation.markets.MarketsScreen


@ExperimentalComposeUiApi
fun NavGraphBuilder.marketsAppGraph(navController: NavHostController) {
    composable(route = Screen.MarketsScreen.route) {
        MarketsScreen(
            showDetails = { marketId ->
                navController.navigate(Screen.DetailsMarketScreen.createRoute(marketId))
            }
        )
    }
    composable(
        route = Screen.DetailsMarketScreen.route
                + "?marketId={marketId}",
        arguments = listOf(
            navArgument(
                name = "marketId"
            ) {
                type = NavType.LongType
                defaultValue = -1L
            }
        )
    ) {
        DetailsMarketScreen(
            navigateUp = {
                navController.navigateUp()
            }
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun MarketNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MarketsScreen.route
    ){
        composable(route = Screen.MarketsScreen.route) {
            MarketsScreen(
                showDetails = { marketId ->
                    navController.navigate(Screen.DetailsMarketScreen.createRoute(marketId))
                }
            )
        }
        composable(
            route = Screen.DetailsMarketScreen.route
                    + "?marketId={marketId}",
            arguments = listOf(
                navArgument(
                    name = "marketId"
                ) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            DetailsMarketScreen(
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }

}