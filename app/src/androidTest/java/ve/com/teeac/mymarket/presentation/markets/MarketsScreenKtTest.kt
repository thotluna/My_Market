package ve.com.teeac.mymarket.presentation.markets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.MarketUseCases
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.navigation.Screen
import ve.com.teeac.mymarket.presentation.theme.MyMarketTheme
import ve.com.teeac.mymarket.utils.TestTags
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class MarketsScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var useCase: MarketUseCases

    private var marketIdCap: Long? = null


    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun getCompose(){
        composeRule.setContent {
            val navController = rememberNavController()
            MyMarketTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.MarketsScreen.route
                ) {
                    composable(route = Screen.MarketsScreen.route) {
                        MarketsScreen(
                            showDetails = {
                                marketIdCap = it
                            }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun initialScreenMarketsEmpty(){
        getCompose()
        composeRule.onNodeWithText(composeRule.activity.applicationContext.getString(R.string.title_app))
            .assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.applicationContext.getString(R.string.markets_empty))
            .assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.LIST_MARKETS).assertDoesNotExist()
    }

    @Test
    fun initialScreenMarketNotEmpty()= runTest{

        useCase.addMarket(
            Market(
                id = 51,
                date = System.currentTimeMillis(),
                amountDollar = 2.5,
                amount = 100.0
            )
        )
        useCase.addMarket(
            Market(
                id = 52,
                date = System.currentTimeMillis(),
                amountDollar = 2.5,
                amount = 100.0
            )
        )
        useCase.addMarket(
            Market(
                id = 53,
                date = System.currentTimeMillis(),
                amountDollar = 2.5,
                amount = 100.0
            )
        )

        getCompose()

        composeRule.onNodeWithText(composeRule.activity.applicationContext.getString(R.string.title_app))
            .assertIsDisplayed()
        composeRule.onNodeWithText(composeRule.activity.applicationContext.getString(R.string.markets_empty))
            .assertDoesNotExist()
        composeRule.onNodeWithTag(TestTags.LIST_MARKETS)
            .onChildren()
             .assertCountEquals(3)

    }

    @Test
    fun itemListMarkets() = runTest{
        useCase.addMarket(
            Market(
                id = 51,
                date = System.currentTimeMillis(),
                amountDollar = 2.5,
                amount = 100.0
            )
        )

        getCompose()

        composeRule.onNodeWithTag("51").assertIsDisplayed()
        composeRule.onNodeWithTag("51").performClick()

        assertThat(marketIdCap).isEqualTo(51)

    }


    fun saveEvent() = runTest{
        getCompose()
        composeRule.onNodeWithContentDescription("Add Market")
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithTag(TestTags.LIST_MARKETS)
            .onChildren()
            .assertCountEquals(1)
    }
}