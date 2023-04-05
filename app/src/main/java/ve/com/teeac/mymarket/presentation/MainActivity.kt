package ve.com.teeac.mymarket.presentation

import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.navigation.MarketNavHost

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyMarket)
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            ProvideWindowInsets {
                MyMarketApp {
                    val navController = rememberNavController()
                    MarketNavHost(navController = navController)
                }
            }
        }
    }



}














