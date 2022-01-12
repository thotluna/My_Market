package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.google.common.truth.Truth.assertThat
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

    private val controller = ProductFormController()

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
                    onSave = { }
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
    fun initialControllerWithProductExistent(){
        val expected = MarketDetail(
            id = 1,
            marketId = 2L,
            quantity = 2.0,
            description = "Cualquier Cosa",
            unitAmountDollar = 10.0,
            unitAmount = 50.0,
            amountDollar = 20.0,
            amount = 100.0
        )

        controller.onEvent(ProductEvent.UpdateProduct(expected))

        assertThat(controller.getMarketDetail(expected.marketId))
            .isEqualTo(expected)

        rules.onNodeWithText("2").assertIsDisplayed()
        rules.onNodeWithText(expected.description).assertIsDisplayed()
        rules.onNodeWithText("10").assertIsDisplayed()
        rules.onNodeWithText("50").assertIsDisplayed()



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

        val newProduct = controller.getMarketDetail(product.marketId)

        assertThat(newProduct).isEqualTo(product)
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

        val newProduct = controller.getMarketDetail(product.marketId)

        assertThat(newProduct).isEqualTo(product.copy(
            unitAmount = product.unitAmountDollar * 5.0,
            amount = (product.unitAmountDollar * 5.0 * product.quantity)*product.quantity
        ))
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

        val newProduct = controller.getMarketDetail(product.marketId)

        assertThat(newProduct).isEqualTo(product)
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

        val newProduct = controller.getMarketDetail(product.marketId)

        assertThat(newProduct).isEqualTo(product.copy(
            unitAmountDollar = product.unitAmount / 5,
            amountDollar = (product.unitAmount / 5) * product.quantity
        ))
    }


}