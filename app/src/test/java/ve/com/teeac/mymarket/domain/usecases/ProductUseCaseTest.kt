package ve.com.teeac.mymarket.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.*
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp

@ExperimentalCoroutinesApi
class ProductUseCaseTest {

    @MockK
    lateinit var repository: DetailMarketRepository

    lateinit var useCase: ProductUseCase


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        useCase = ProductUseCase(
            addProduct = AddProduct(repository),
            getAllProducts = GetAllProducts(repository),
            getProduct = GetProduct(repository),
            deleteProduct = DeleteProduct(repository),
            updateProducts = UpdateProductByRate(repository)
        )
    }

    @Test
    fun add_new_product() = runTest {
        coEvery { repository.addProduct(any()) } returns (1L)

        useCase.addProduct(MarketDetail(marketId = 2L, description = "testing"))

        coVerify(exactly = 1) { repository.addProduct(any()) }
        confirmVerified(repository)

    }

    @Test
    fun getAll_product_emptyList() = runTest {
        coEvery { repository.getAllProducts(any()) } returns (flow { emit(emptyList<MarketDetail>()) })

        val list = useCase.getAllProducts(1L)

        assertThat(list.first().count()).isEqualTo(0)

        coVerify(exactly = 1) { repository.getAllProducts(1L) }
        confirmVerified(repository)
    }

    @Test
    fun getAll_product_fullList() = runTest {
        coEvery { repository.getAllProducts(any()) } returns (flow {
            emit(
                listOf(
                    MarketDetail(
                        marketId = 1L,
                        description = "test"
                    )
                )
            )
        })

        val list = useCase.getAllProducts(1L)

        assertThat(list.first().count()).isEqualTo(1)

        coVerify(exactly = 1) { repository.getAllProducts(1L) }
        confirmVerified(repository)
    }

    @Test
    fun get_product_exist() = runTest {
        val expected = MarketDetail(marketId = 1L, id = 1L, description = "test")
        coEvery { repository.getProduct(any()) } returns (expected)
        val product = useCase.getProduct(expected.id!!)

        assertThat(product).isEqualTo(expected)

        coVerify(exactly = 1) { repository.getProduct(expected.id!!) }
        confirmVerified(repository)
    }

    @Test
    fun get_product_does_not_exist() = runTest {
        coEvery { repository.getProduct(any()) }.throws(InvalidPropertyApp(InvalidPropertyApp.PRODUCT_DOES_NOT_EXIST))

        try {
            val product = useCase.getProduct(1L)
        } catch (e: InvalidPropertyApp) {
            assertThat(e.message).isEqualTo(InvalidPropertyApp.PRODUCT_DOES_NOT_EXIST)
        }

        coVerify(exactly = 1) { repository.getProduct(1L) }
        confirmVerified(repository)

    }

    @Test
    fun delete_product_exist() = runTest {
        coEvery { repository.deleteProduct(any()) } returns Unit

        useCase.deleteProduct(1L)

        coVerify(exactly = 1) { repository.deleteProduct(1L) }
        confirmVerified(repository)
    }


    @Test
    fun update_product_by_rate() = runTest {

        val rate = 5.0
        val list = getList()
        coEvery { repository.getAllProducts(1L) } returns (
                flow {
                    emit(
                        list
                    )
                }
                )

        coEvery { repository.addListProduct(any()) } returns Unit


        useCase.updateProducts(rate, marketId = 1L)

        val listRes = useCase.getAllProducts(1L).first().first()

        assertThat(listRes.unitAmount).isEqualTo(list.first().unitAmountDollar * rate)
        assertThat(listRes.amount).isEqualTo(list.first().amountDollar * rate)

        coVerify(exactly = 2) { repository.getAllProducts(1L) }
        coVerify(exactly = 1) { repository.addListProduct(any()) }
        confirmVerified(repository)
    }

    @Test
    fun update_product_by_rate_equal_zero() = runTest {
        val rate = 0.0
        val list = getList()
        coEvery { repository.getAllProducts(1L) } returns (
                flow {
                    emit(
                        list
                    )
                }
                )

        coEvery { repository.addListProduct(any()) } returns Unit


        useCase.updateProducts(rate, marketId = 1L)

        assertThat(useCase.getAllProducts(1L).first()).isEqualTo(list)

        coVerify(exactly = 2) { repository.getAllProducts(1L) }
        coVerify(exactly = 1) { repository.addListProduct(any()) }
        confirmVerified(repository)
    }

    private fun getList(): List<MarketDetail>{
        return listOf(
            MarketDetail(
                marketId = 1L,
                id = 1L,
                quantity = 1.0,
                description = "test",
                unitAmountDollar = 5.0,
                amountDollar = 5.0
            ),
            MarketDetail(
                marketId = 1L,
                id = 2L,
                quantity = 1.0,
                description = "test",
                unitAmountDollar = 5.0,
                amountDollar = 5.0
            )
        )
    }


}