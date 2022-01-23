package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
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
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.presentation.MainActivity
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.utils.TestTags
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class ProductFormKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @ExperimentalComposeUiApi
    @get:Rule(order = 1)
    val rules = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var controller: ProductFormController

    @Before
    fun setUp() {
        hiltRule.inject()
        rules.setContent {
            MyMarketApp {
                ProductForm(
                    quantity = controller.quantity.value,
                    quantityChange = {
                            controller.onEvent(ProductEvent.EnteredQuantity(it))
                        },
                    description = controller.description.value,
                    descriptionChange = {
                            controller.onEvent(ProductEvent.EnteredDescription(it))
                        },
                    amountBs = controller.amountBs.value,
                    amountBsChange = {
                            controller.onEvent(ProductEvent.EnteredAmountBs(it))
                        },
                    amountDollar = controller.amountDollar.value,
                    amountDollarChange = {
                            controller.onEvent(ProductEvent.EnteredAmountDollar(it))
                        },
                    onSave = { },
                    persistent = controller.persistentShowSection.value,
                    changePersistent = {controller.onChangedPersistent()}
                )

            }

        }

    }

    @Test
    fun initialController(){
        rules.onNodeWithText("Cantidad").assertIsDisplayed()
        rules.onNodeWithText("Descripción").assertIsDisplayed()
        rules.onNodeWithText("Bs").assertIsDisplayed()
        rules.onNodeWithText("$").assertIsDisplayed()

        rules.onNodeWithTag(TestTags.QUALITY_FIELD).performTextInput("1")
        rules.onNodeWithTag(TestTags.DESCRIPTION_FIELD).performTextInput("2")
        rules.onNodeWithTag(TestTags.AMOUNT_BS_FIELD).performTextInput("3")
        rules.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD).performTextInput("4")


        rules.onNodeWithText("Cantidad").assertIsDisplayed()
        rules.onNodeWithText("Descripción").assertIsDisplayed()
        rules.onNodeWithText("Bs").assertIsDisplayed()
        rules.onNodeWithText("$").assertIsDisplayed()

        rules.onNodeWithText("1").assertIsDisplayed()
        rules.onNodeWithText("2").assertIsDisplayed()
        rules.onNodeWithText("0").assertIsDisplayed()
        rules.onNodeWithText("4").assertIsDisplayed()

    }

    @Test
    fun addNewProductDollarWithoutRate(){
        val product = MarketDetail(
            id = null,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmountDollar = 50.0,
            amountDollar = 50.0
        )
        rules.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        rules.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        rules.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD)
            .performTextInput(product.unitAmountDollar.toString())

    }

    @Test
    fun addNewProductDollarRate(){
        val product = MarketDetail(
            id = null,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmountDollar = 50.0,
            amountDollar = 50.0
        )
        controller.onEvent(ProductEvent.UpdateRate(5))
        rules.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        rules.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        rules.onNodeWithTag(TestTags.AMOUNT_DOLLAR_FIELD)
            .performTextInput(product.unitAmountDollar.toString())
    }

    @Test
    fun addNewProductBsWithoutRate(){
        val product = MarketDetail(
            id = null,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmount = 50.0,
            amount = 50.0
        )
        controller.clear()
        rules.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        rules.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        rules.onNodeWithTag(TestTags.AMOUNT_BS_FIELD)
            .performTextInput(product.unitAmount.toString())
    }

    @Test
    fun addNewProductBsRate(){
        val product = MarketDetail(
            id = null,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmount = 50.0,
            amount = 50.0
        )
        controller.onEvent(ProductEvent.UpdateRate(5))
        rules.onNodeWithTag(TestTags.QUALITY_FIELD)
            .performTextInput(product.quantity.toString())
        rules.onNodeWithTag(TestTags.DESCRIPTION_FIELD)
            .performTextInput(product.description)
        rules.onNodeWithTag(TestTags.AMOUNT_BS_FIELD)
            .performTextInput(product.unitAmount.toString())

    }




}