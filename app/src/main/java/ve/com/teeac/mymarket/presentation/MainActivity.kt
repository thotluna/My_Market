package ve.com.teeac.mymarket.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ve.com.teeac.mymarket.presentation.navigation.Screen
import ve.com.teeac.mymarket.presentation.navigation.marketsAppGraph
import ve.com.teeac.mymarket.presentation.components.MyMarketApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyMarketApp {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.MarketsScreen.route
                ) {
                    marketsAppGraph(navController)
                }
            }
        }
    }
}














