package ve.com.teeac.mymarket.presentation.marketdetails

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

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
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
                        DetailsMarketScreen(navigateUp = { /*TODO*/ })
                    }
                }

            }
        }
    }

    @Test
    fun initialBasic() {
        composeRule.onNodeWithContentDescription("Back")
        composeRule.onNodeWithText("Mercado -1").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Open Setup Section").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Open Product Section").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("AmountSetupForm").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("ProductForm").assertDoesNotExist()
    }

    @Test
    fun saveNewSetup() {
        composeRule.onNodeWithContentDescription("AmountSetupForm").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Open Setup Section").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open Setup Section").performClick()
        composeRule.onNodeWithContentDescription("AmountSetupForm").assertIsDisplayed()
        composeRule.onNodeWithText("Taza").assertIsDisplayed()
        composeRule.onNodeWithText("Bs").assertIsDisplayed()
        composeRule.onNodeWithText("$").assertIsDisplayed()

        composeRule.onNodeWithText("Taza").performTextInput("5")
        composeRule.onNodeWithText("$").performTextInput("100")
        composeRule.onNodeWithText("500").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Guardar setup").performClick()
        composeRule.onNodeWithContentDescription("AmountSetupForm").assertDoesNotExist()
    }

    @Test
    fun saveNewProductWithoutSetup() {

        composeRule.onNodeWithContentDescription("ProductForm").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Open Product Section").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Open Product Section").performClick()
        composeRule.onNodeWithContentDescription("ProductForm").assertIsDisplayed()

        composeRule.onNodeWithText("Cantidad").assertIsDisplayed()
        composeRule.onNodeWithText("Descripción").assertIsDisplayed()
        composeRule.onNodeWithText("Bs").assertIsDisplayed()
        composeRule.onNodeWithText("$").assertIsDisplayed()


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

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 $"))
                .assert(hasText("0.00 Bs"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 $"))
                .assertHasClickAction()
        }
    }

    @Test
    fun updateProductByRateSetup(){
        composeRule.onNodeWithContentDescription("Open Product Section").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("$").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 $"))
                .assert(hasText("0.00 Bs"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 $"))
                .assertHasClickAction()
        }

        composeRule.onNodeWithContentDescription("Open Setup Section").performClick()
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput("100")
        composeRule.onNodeWithContentDescription("Guardar setup").performClick()

        composeRule.onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
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
    fun editProductWithoutRate(){
        composeRule.onNodeWithContentDescription("Open Product Section").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("Bs").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 Bs"))
                .assert(hasText("0.00 $"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 Bs"))
                .assertHasClickAction()
        }

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
    fun editProductWithRate(){

        composeRule.onNodeWithContentDescription("Open Setup Section").performClick()
        composeRule.onNodeWithTag(TestTags.RATE_FIELD).performTextInput("5")
        composeRule.onNodeWithTag(TestTags.MAX_DOLLAR).performTextInput("100")
        composeRule.onNodeWithContentDescription("Guardar setup").performClick()

        composeRule.onNodeWithContentDescription("Open Product Section").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("Bs").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
                .assert(hasText("2.0"))
                .assert(hasText("papa"))
                .assert(hasText("Por Unidad"))
                .assert(hasText("2.80 Bs"))
                .assert(hasText("0.56 $"))
                .assert(hasText("Total"))
                .assert(hasText("5.60 Bs"))
                .assert(hasText("1.12 $"))
                .assertHasClickAction()
        }

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
    fun deleteProduct(){

        composeRule.onNodeWithContentDescription("Open Product Section").performClick()
        composeRule.onNodeWithText("Cantidad").performTextInput("2")
        composeRule.onNodeWithText("Descripción").performTextInput("papa")
        composeRule.onNodeWithText("$").performTextInput("2.8")
        composeRule.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performImeAction()

        composeRule.apply {
            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .assertCountEquals(1)

            onNodeWithTag(TestTags.LIST_PRODUCT)
                .onChildren()
                .onFirst()
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


}