package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.*
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
class ProductFormControllerTest {

    private val standardTestDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var addProduct: AddProduct

    @MockK
    lateinit var getAllProducts: GetAllProducts

    @MockK
    lateinit var getProduct: GetProduct

    @MockK
    lateinit var deleteProduct: DeleteProduct

    @MockK
    lateinit var updateProducts: UpdateProductByRate

    lateinit var useCase: ProductUseCase
    private lateinit var controller: ProductFormController


    private val product = MarketDetail(
        id = 1L,
        marketId = 1L,
        quantity = 1.0,
        description = "Cosa",
        unitAmount = 50.0,
        amount = 50.0
    )

    private val number: Number = 5

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        useCase = ProductUseCase(
            addProduct = addProduct,
            getAllProducts = getAllProducts,
            getProduct = getProduct,
            deleteProduct = deleteProduct,
            updateProducts = updateProducts
        )

        controller = ProductFormController(useCase, standardTestDispatcher, Dispatchers.IO)

    }

    @Test
    fun initialControllerEmpty() {
        assertThat(controller.quantity.value.number).isNull()
        assertThat(controller.description.value.text).isNull()
        assertThat(controller.amountBs.value.number).isNull()
        assertThat(controller.amountDollar.value.number).isNull()
    }

    @Test
    fun loadProductExist() {
        coEvery { useCase.getProduct(any()) } coAnswers { product }

        runTest {
            controller.loadProduct(product.id!!)
        }

        assertThat(controller.quantity.value.number).isEqualTo(product.quantity)
        assertThat(controller.description.value.text).isEqualTo(product.description)
        assertThat(controller.amountBs.value.number).isEqualTo(product.unitAmount)
        assertThat(controller.amountDollar.value.number).isEqualTo(product.unitAmountDollar)

        coVerify(exactly = 1) { useCase.getProduct(product.marketId) }
        confirmVerified(useCase.getProduct)
    }

    @Test
    fun createNewProductWithoutRate() = runTest {

        coEvery { useCase.addProduct(any()) } coAnswers { product.id }

        controller.onEvent(ProductEvent.EnteredQuantity(product.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(product.description))
        controller.onEvent(ProductEvent.EnteredAmountBs(product.unitAmount))

        controller.saveProduct(product.marketId)

        coVerify(exactly = 1) { useCase.addProduct(product.copy(id = null)) }
        confirmVerified(useCase.addProduct)
    }

    @Test
    fun `validate auto modify amountDollar and unitAmountDollar`() = runTest {
        val rate = 3
        val expected = product.copy(
            id = null,
            unitAmountDollar = ((product.unitAmount / rate) * 1000.0).roundToInt() / 1000.0,
            amountDollar =  ((((product.unitAmount / rate)) * 1000.0).roundToInt() / 1000.0) * product.quantity
        )
        val sendProduct = slot<MarketDetail>()
        coEvery { useCase.addProduct(capture(sendProduct)) } coAnswers { product.id }
        setRate(rate)
        delay(500L)
        controller.onEvent(ProductEvent.EnteredQuantity(expected.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(expected.description))
        controller.onEvent(ProductEvent.EnteredAmountBs(expected.unitAmount))

        controller.saveProduct(expected.marketId)

        assertThat(sendProduct.captured).isEqualTo(expected)

        coVerify(exactly = 1) { useCase.addProduct(expected) }
        confirmVerified(useCase.addProduct)

    }

    @Test
    fun `validate auto modify amount and unitAmount`() = runTest {
        val rate = 6
        val expected = product.copy(
            id = null,
            unitAmountDollar = 100.0,
            amountDollar = 100.0,
            unitAmount = 100.0 * rate,
            amount = 100.0 * rate
        )
        setRate(rate)
        val sendProduct = slot<MarketDetail>()
        coEvery { useCase.addProduct(capture(sendProduct)) } coAnswers { product.id }
        delay(500L)
        controller.onEvent(ProductEvent.EnteredQuantity(expected.quantity))
        controller.onEvent(ProductEvent.EnteredDescription(expected.description))
        controller.onEvent(ProductEvent.EnteredAmountDollar(expected.unitAmountDollar))

        controller.saveProduct(expected.marketId)

        assertThat(sendProduct.captured).isEqualTo(expected)

        coVerify(exactly = 1) { useCase.addProduct(expected) }
        confirmVerified(useCase.addProduct)

    }

    @Test
    fun clearedForm() {
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

    @Test
    fun `Modify all product with idMarket by rate`() = runTest {

        val rate = 2.0

        coEvery { useCase.updateProducts(rate, product.marketId) } returns Unit

        controller.loadIdMarket(product.marketId)

        controller.onEvent(ProductEvent.UpdateRate(rate))

        delay(100L)

        coVerify { useCase.updateProducts(rate, product.marketId) }

        confirmVerified(useCase.updateProducts)
    }

    @Test
    fun `Persist section`() = runTest {
        coEvery { useCase.addProduct(any()) } returns Unit

        assertThat(controller.persistentShowSection.value).isEqualTo(false)
        assertThat(controller.isSectionVisible.value).isEqualTo(false)

        controller.onChangedPersistent()
        controller.onToggleSection()

        controller.saveProduct(1L)

        assertThat(controller.persistentShowSection.value).isEqualTo(true)
        assertThat(controller.isSectionVisible.value).isEqualTo(true)

        controller.onChangedPersistent()

        assertThat(controller.persistentShowSection.value).isEqualTo(false)
        assertThat(controller.isSectionVisible.value).isEqualTo(true)

        controller.saveProduct(1L)

        assertThat(controller.isSectionVisible.value).isEqualTo(false)

    }


    private fun setRate(number: Number) {
        coEvery { useCase.updateProducts(any(), any()) } returns Unit
        controller.loadIdMarket(product.marketId)
        controller.onEvent(ProductEvent.UpdateRate(number))
    }




}