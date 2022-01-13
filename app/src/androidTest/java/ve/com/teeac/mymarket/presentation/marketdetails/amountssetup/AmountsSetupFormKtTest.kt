package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.usecases.SetupUseCase
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class AmountsSetupFormKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var useCase: SetupUseCase

    lateinit var controller: SetupController

    val monto = "56258.23"


    @Before
    fun setUp() {
        hiltRule.inject()
        controller = SetupController(valueInitial = AmountsSetup(), useCase = useCase)
        composeRule.setContent {
            MyMarketApp {
               AmountSetupForm(
                   converterState = AmountSetupState(controller.rate.value) {
                       controller.onEvent(
                           AmountSetupEvent.EnteredRate(it)
                       )
                   },
                   amountState =AmountSetupState(controller.maxBolivares.value) {
                       controller.onEvent(
                           AmountSetupEvent.EnteredMaxBolivares(it)
                       )
                   },
                   amountsDollarState =AmountSetupState(controller.maxDollar.value) {
                       controller.onEvent(
                           AmountSetupEvent.EnteredMaxDollar(it)
                       )
                   },
                   onToggleSetupSection = {},
                   onSave = {}
               )
            }
        }
    }

    @Test
    fun initialComponent(){
        composeRule.onNodeWithText(SetupController.NAME_RATE).assertIsDisplayed()
        composeRule.onNodeWithText(SetupController.NAME_DOLLAR).assertIsDisplayed()
        composeRule.onNodeWithText(SetupController.NAME_BOLIVARES).assertIsDisplayed()
    }

    @Test
    fun loadDataInRate(){
        composeRule.onNodeWithText(SetupController.NAME_RATE)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${SetupController.NAME_RATE}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }

    @Test
    fun loadDataInBs(){
        composeRule.onNodeWithText(SetupController.NAME_BOLIVARES)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${SetupController.NAME_BOLIVARES}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }

    @Test
    fun loadDataInDollar(){
        composeRule.onNodeWithText(SetupController.NAME_DOLLAR)
            .assertIsDisplayed()
            .performTextInput(monto)
        composeRule.onNodeWithText(monto).assertIsDisplayed()

        composeRule.onNodeWithTag("Tag_Field_${SetupController.NAME_DOLLAR}").performTextClearance()
        composeRule.onNodeWithText(monto).assertDoesNotExist()
    }
}