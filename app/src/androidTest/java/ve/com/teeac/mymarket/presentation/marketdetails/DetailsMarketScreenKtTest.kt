package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.navigation.Screen
import ve.com.teeac.mymarket.utils.TestTags

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class DetailsMarketScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @ExperimentalMaterialApi
    @Before
    fun setUp() {

        hiltRule.inject()

        composeRule.setContent {
            MyMarketApp {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.DetailsMarketScreen.route
                ) {
                    composable(route = Screen.DetailsMarketScreen.route) {
                        DetailsMarketScreen(navigateUp = { })
                    }
                }

            }
        }
    }

    @Test
    fun initialBasic() {
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.button_back))
        composeRule.onNodeWithText("${appContext.getString(R.string.app_name)} -1").assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.button_open_hide_setup_section)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section))
            .assertIsDisplayed()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.setup_section)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertDoesNotExist()
    }

    @Test
    fun saveNewSetup() {
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.setup_section)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.button_open_hide_setup_section)).assertIsDisplayed()

        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.button_open_hide_setup_section)).performClick()
        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.setup_section)).assertIsDisplayed()
        composeRule.onNodeWithText("Taza").assertIsDisplayed()
        composeRule.onNodeWithText("Bs").assertIsDisplayed()
        composeRule.onNodeWithText("$").assertIsDisplayed()

        composeRule.onNodeWithText("Taza").performTextInput("5")
        composeRule.onNodeWithText("$").performTextInput("100")
        composeRule.onNodeWithText("500").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Guardar setup").performClick()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.setup_section)).assertDoesNotExist()
    }

    @Test
    fun saveNewProductWithoutSetup() {


        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertDoesNotExist()
        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).assertIsDisplayed()

        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertIsDisplayed()

        composeRule.onNodeWithText("Cantidad").assertIsDisplayed()
        composeRule.onNodeWithText("Descripción").assertIsDisplayed()
        composeRule.onNodeWithText("Bs").assertIsDisplayed()
        composeRule.onNodeWithText("$").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()


        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText("2"))
        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assertIsFocused()

        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText("papa"))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assertIsFocused()

        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assertIsFocused()

        composeRule.onNodeWithText("$").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText("2.8"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText("0"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()
        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assertIsFocused()

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText(text = ""))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText(""))


        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT)
            .onChildren()
            .assertCountEquals(1)

        composeRule.onNodeWithContentDescription("item papa")
            .assertHasClickAction()
            .assert(hasText("2.0"))
            .assert(hasText("papa"))
            .assert(hasText("Por Unidad"))
            .assert(hasText("2.80 $"))
            .assert(hasText("0.00 Bs"))
            .assert(hasText("Total"))
            .assert(hasText("5.60 $"))


    }

    @Test
    fun updateProductByRateSetup() {
        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("$").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT)
            .onChildren()
            .assertCountEquals(1)

        composeRule.onNodeWithContentDescription("item papa")
            .assert(hasText("2.0"))
            .assert(hasText("papa"))
            .assert(hasText("Por Unidad"))
            .assert(hasText("2.80 $"))
            .assert(hasText("0.00 Bs"))
            .assert(hasText("Total"))
            .assert(hasText("5.60 $"))
            .assertHasClickAction()

        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.button_open_hide_setup_section)).performClick()
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput("100")
        composeRule.onNodeWithContentDescription("Guardar setup").performClick()

        composeRule.onNodeWithContentDescription("item papa")
            .assert(hasText("2.0"))
            .assert(hasText("papa"))
            .assert(hasText("Por Unidad"))
            .assert(hasText("2.80 $"))
            .assert(hasText("14.00 Bs"))
            .assert(hasText("Total"))
            .assert(hasText("5.60 $"))
            .assert(hasText("28.00 Bs"))
            .assertHasClickAction()
    }

    @Test
    fun editProductWithoutRate() {
        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("Bs").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT)
            .onChildren()
            .assertCountEquals(1)

        composeRule.onNodeWithContentDescription("item papa")
            .assert(hasText("2.0"))
            .assert(hasText("papa"))
            .assert(hasText("Por Unidad"))
            .assert(hasText("2.80 Bs"))
            .assert(hasText("0.00 $"))
            .assert(hasText("Total"))
            .assert(hasText("5.60 Bs"))
            .assertHasClickAction()


        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText(text = ""))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText(""))

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT).onChildren().onFirst().performClick()

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText(text = "2"))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText("papa"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText("0"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText("2.8"))


    }

    @Test
    fun editProductWithRate() {

        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.button_open_hide_setup_section)).performClick()
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput("100")
        composeRule.onNodeWithContentDescription("Guardar setup").performClick()

        composeRule.onNodeWithContentDescription(appContext.resources
                .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("Bs").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

        composeRule.onNodeWithContentDescription("item papa")
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 Bs"))
                .assert(hasText("0.56 $"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 Bs"))
                .assert(hasText("1.12 $"))
                .assertHasClickAction()


        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText(text = ""))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText(""))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText(""))

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT).onChildren().onFirst().performClick()

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD).assert(hasText(text = "2"))
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD).assert(hasText("papa"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).assert(hasText("0.56"))
        composeRule.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).assert(hasText("2.8"))


    }

    @Test
    fun deleteProduct() {

        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("$").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            composeRule.onNodeWithContentDescription("item papa")
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 $"))
                .assert(hasText("0.00 Bs"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 $"))
                .assertHasClickAction()

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
                .performGesture {
                    swipeLeft()
                }

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(0)

        }

    }

    @Test
    fun validateTotals() {


        val maxDollar = 5.0
        val listProduct = listOf(
            MarketDetail(
                quantity = 5.0,
                description = "Huevos",
                unitAmountDollar = 0.25,
                amountDollar = 1.25,
                marketId = 1L
            ),
            MarketDetail(
                quantity = 2.0,
                description = "Refresco",
                unitAmountDollar = 1.625,
                amountDollar = 3.25,
                marketId = 1L
            ),
            MarketDetail(
                quantity = 1.0,
                description = "aceite",
                unitAmountDollar = 3.25,
                amountDollar = 3.25,
                marketId = 1L
            )
        )


        composeRule.onNodeWithContentDescription("Totals")
            .onChildren()
            .assertAny(hasText("Totals"))
            .assertAny(hasText("0.00 Bs"))
            .assertAny(hasText("0.00 Bs"))

        composeRule.onNodeWithContentDescription(appContext
            .getString(R.string.button_open_hide_setup_section)).performClick()
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput(maxDollar.toString())
        composeRule.onNodeWithContentDescription("Guardar setup").performClick()

        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()
        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(listProduct[0].quantity.toString())
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(listProduct[0].description)
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD)
            .performTextInput(listProduct[0].unitAmountDollar.toString())
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithContentDescription("Totals")
            .onChildren()
            .assertAny(hasText("6.25 Bs"))
            .assertAny(hasText("1.25 $"))


        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(listProduct[1].quantity.toString())
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(listProduct[1].description)
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD)
            .performTextInput(listProduct[1].unitAmountDollar.toString())
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithContentDescription("Totals")
            .onChildren()
            .assertAny(hasText("22.50 Bs"))
            .assertAny(hasText("4.50 $"))

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(listProduct[2].quantity.toString())
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(listProduct[2].description)
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD)
            .performTextInput(listProduct[2].unitAmountDollar.toString())
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithContentDescription("Totals")
            .onChildren()
            .assertAny(hasText("38.75 Bs"))
            .assertAny(hasText("7.75 $"))


    }

    @Test
    fun persistentSection() {

        val product = MarketDetail(
            id = null,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmount = 50.0,
            amount = 50.0
        )
        composeRule.onNodeWithContentDescription(appContext.resources
            .getString(R.string.button_open_product_section)).performClick()
        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Mantener seccion").performClick()

        composeRule.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        composeRule.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.onNodeWithContentDescription(appContext.getString(R.string.product_form_section)).assertDoesNotExist()

    }


}