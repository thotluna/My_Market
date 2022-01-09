package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.markets.MarketsScreen
import ve.com.teeac.mymarket.presentation.navigation.Screen
import ve.com.teeac.mymarket.presentation.theme.MyMarketTheme

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class AmountsSetupFormKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    lateinit var controller: AmountSetupController

    val monto = "56258.23"

    @Before
    fun setUp() {
        hiltRule.inject()
        controller = AmountSetupController()
        composeRule.setContent {
            MyMarketApp {
               AmountSetupForm(
                   converterState = AmountSetupState(controller.rate.value) {
                       controller.onAmountsSetupEvent(
                           AmountSetupEvent.EnteredConvert(it)
                       )
                   },
                   amountState =AmountSetupState(controller.maxBolivares.value) {
                       controller.onAmountsSetupEvent(
                           AmountSetupEvent.EnteredAmounts(it)
                       )
                   },
                   amountsDollarState =AmountSetupState(controller.maxDollar.value) {
                       controller.onAmountsSetupEvent(
                           AmountSetupEvent.EnteredAmountsDollar(it)
                       )
                   }
               )
            }
        }
    }

    @Test
    fun initialComponent(){
        composeRule.onNodeWithText(AmountSetupController.NAME_RATE).assertIsDisplayed()
        composeRule.onNodeWithText(AmountSetupController.NAME_DOLLAR).assertIsDisplayed()
        composeRule.onNodeWithText(AmountSetupController.NAME_BOLIVARES).assertIsDisplayed()
    }

    @Test
    fun loadDataInRate(){
        composeRule.onNodeWithText(AmountSetupController.NAME_RATE)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${AmountSetupController.NAME_RATE}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }

    @Test
    fun loadDataInBs(){
        composeRule.onNodeWithText(AmountSetupController.NAME_BOLIVARES)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${AmountSetupController.NAME_BOLIVARES}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }

    @Test
    fun loadDataInDollar(){
        composeRule.onNodeWithText(AmountSetupController.NAME_DOLLAR)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${AmountSetupController.NAME_DOLLAR}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }
}