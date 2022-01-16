package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.SetupUseCase
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.utils.TestTags
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class AmountsSetupFormKtTest {

    private val standardTestDispatcher = StandardTestDispatcher()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var useCase: SetupUseCase

    private lateinit var controller: SetupController

    private val amount = "56258.23"


    @Before
    fun setUp() {
        hiltRule.inject()
        controller = SetupController(
            valueInitial = AmountsSetup(),
            useCase = useCase,
            dispatcher = standardTestDispatcher,
            dispatcherIo = standardTestDispatcher
        )
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
                   onSave = {}
               )
            }
        }
    }

    @Test
    fun initialCompose(){
        composeRule.onNodeWithContentDescription("AmountSetupForm").assertIsDisplayed()
        composeRule.onNodeWithText("Taza").assertIsDisplayed()
        composeRule.onNodeWithText("Bs").assertIsDisplayed()
        composeRule.onNodeWithText("$").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Guardar setup").assertIsDisplayed()
    }

    @Test
    fun fillFormWithoutBolivares(){
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).assertIsFocused()
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).performTextInput("5")

        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).assert(hasText("1"))
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).assert(hasText("5"))
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).assert(hasText("5"))
    }

    @Test
    fun fillFormWithoutDollar(){
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).assertIsFocused()
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).performImeAction()
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).assertIsFocused()
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput("5")

        composeRule.onNodeWithTag(TestTags.RATE_FIELD).assert(hasText("5"))
        composeRule.onNodeWithTag(TestTags.MAX_BOLIVARES).assert(hasText("25"))
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).assert(hasText("5"))
    }
}