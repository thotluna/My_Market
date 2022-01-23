package ve.com.teeac.mymarket.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.DetailsMarketUseCase
import ve.com.teeac.mymarket.domain.usecases.SetupUseCase
import ve.com.teeac.mymarket.presentation.MainActivity
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class MarketNavHostKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var useCaseDetail: DetailsMarketUseCase
    @Inject
    lateinit var useCaseSetup: SetupUseCase

    private lateinit var navController: NavHostController

    private val market = Market(id = 1L)

    @Before
    fun setUp() {
        hiltRule.inject()

        runBlocking{
            useCaseDetail.addMarket(market)
            useCaseSetup.addSetup(AmountsSetup(marketId = market.id))
        }
        composeRule.setContent {
            navController = rememberNavController()
            MarketNavHost(navController = navController)
        }
    }

    @Test
    fun marketNavHostTest() {
        composeRule.onNodeWithContentDescription("Market Screen").assertIsDisplayed()
    }

    @Test
    fun marketNavigateToNewDetailAndBack() {
        composeRule.onNodeWithContentDescription("Market Screen").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Add Market").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Add Market").performClick()

        composeRule.onNodeWithContentDescription("Detail Market Screen").assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.button_back)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.button_back)).performClick()

        composeRule.onNodeWithContentDescription("Market Screen").assertIsDisplayed()
    }
    @Test
    fun marketNavigateToDetailAndBack() {

        composeRule.onNodeWithContentDescription("Market Screen").assertIsDisplayed()
        composeRule.onNodeWithTag(market.id.toString()).assertIsDisplayed()
        composeRule.onNodeWithTag(market.id.toString()).performClick()

        composeRule.onNodeWithContentDescription("Detail Market Screen").assertIsDisplayed()
        composeRule.onNodeWithText("${appContext.getString(R.string.app_name)} 1").assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.button_back)).performClick()

        composeRule.onNodeWithContentDescription("Market Screen").assertIsDisplayed()

    }

}