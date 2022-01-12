package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.MarketDetail

class ProductFormControllerTest {

    private lateinit var controller: ProductFormController
    private lateinit var controllerFull: ProductFormController

    val product = MarketDetail(
        id = 1L,
        marketId = 1L,
        quantity = 1.0,
        description = "Cosa",
        unitAmount = 50.0,
        amount = 50.0
    )

    val number: Number = 5

    @Before
    fun setUp() {
        controller = ProductFormController()

        controllerFull = ProductFormController()
    }

    @Test
    fun initialControllerEmpty() {
        assertThat(controller.quantity.value.number).isNull()
        assertThat(controller.description.value.text).isNull()
        assertThat(controller.amountBs.value.number).isNull()
        assertThat(controller.amountDollar.value.number).isNull()
    }

    @Test
    fun loadProductExist(){
        controller.onEvent(ProductEvent.UpdateProduct(product))

        val equalProduct = controller.getMarketDetail(product.marketId)

        assertThat(equalProduct).isEqualTo(product)
    }

    @Test
    fun createNewProduct(){
        controller.onEvent(ProductEvent.EnteredQuantity(product.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(product.description))
        controller.onEvent(ProductEvent.EnteredAmountBs(product.unitAmount))

        assertThat(controller.getMarketDetail(product.marketId)).isEqualTo(
            product.copy(id = null)
        )
    }

    @Test
    fun createNewProductBsWithRate(){
        setRate(number)
        controller.onEvent(ProductEvent.EnteredQuantity(product.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(product.description))
        controller.onEvent(ProductEvent.EnteredAmountBs(product.unitAmount))

        assertThat(controller.getMarketDetail(product.marketId)).isEqualTo(
            product.copy(
                id = null,
                unitAmountDollar = product.unitAmount / number.toDouble(),
                amountDollar = (product.unitAmount / number.toDouble()) * product.quantity,
            )
        )
    }

    @Test
    fun createNewProductDollarWithRate(){
        val product = MarketDetail(
            id = 1L,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmountDollar = 50.0,
            amountDollar = 50.0
        )
        setRate(number)
        controller.onEvent(ProductEvent.EnteredQuantity(product.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(product.description))
        controller.onEvent(ProductEvent.EnteredAmountDollar(product.unitAmountDollar))

        assertThat(controller.getMarketDetail(product.marketId)).isEqualTo(
            product.copy(
                id = null,
                unitAmount = product.unitAmountDollar * number.toDouble(),
                amount = (product.unitAmountDollar * number.toDouble()) * product.quantity,
            )
        )
    }

    @Test
    fun clearedForm(){
        val product = MarketDetail(
            id = 1L,
            marketId = 1L,
            quantity = 1.0,
            description = "Cosa",
            unitAmountDollar = 50.0,
            amountDollar = 50.0
        )
        setRate(number)
        controller.onEvent(ProductEvent.EnteredQuantity(product.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(product.description))
        controller.onEvent(ProductEvent.EnteredAmountDollar(product.unitAmountDollar))

        assertThat(controller.id.value).isNull()
        assertThat(controller.quantity.value.number).isNotNull()
        assertThat(controller.description.value.text).isNotNull()
        assertThat(controller.amountBs.value.number).isNotNull()
        assertThat(controller.amountDollar.value.number).isNotNull()

        controller.clear()

        assertThat(controller.id.value).isNull()
        assertThat(controller.quantity.value.number).isNull()
        assertThat(controller.description.value.text).isNull()
        assertThat(controller.amountBs.value.number).isNull()
        assertThat(controller.amountDollar.value.number).isNull()
    }


    private fun setRate(number: Number){
        controller.onEvent(ProductEvent.UpdateRate(number))
    }



}