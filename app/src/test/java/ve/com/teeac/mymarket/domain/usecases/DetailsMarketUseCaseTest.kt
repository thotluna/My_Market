package ve.com.teeac.mymarket.domain.usecases

import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.ActivatedProduct
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.GetAllProducts

@ExperimentalCoroutinesApi
class DetailsMarketUseCaseTest {

    @MockK
    lateinit var repository: DetailMarketRepository

    @MockK
    lateinit var repositoryMarkets: MarketsRepository

    lateinit var useCase: DetailsMarketUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        useCase = DetailsMarketUseCase(
            addMarket = AddMarket(repositoryMarkets),
            getAllProducts = GetAllProducts(repository),
            updateActivatedProduct = ActivatedProduct(repository)
        )
    }

    @Test
    fun add_new_Market() = runTest {
        val slot = slot<Market>()
        coEvery { repositoryMarkets.addMarket(capture(slot)) } returns(1L)

        useCase.addMarket(Market())

        Truth.assertThat(slot.captured.copy(date = 0L)).isEqualTo(Market( date = 0L))

        coVerify(exactly = 1){ repositoryMarkets.addMarket(any())}
        confirmVerified(repositoryMarkets)
    }

    @Test
    fun getAll_product_emptyList() = runTest {
        coEvery { repository.getAllProducts(any()) } returns (flow { emit(emptyList<MarketDetail>()) })

        val list = useCase.getAllProducts(1L)

        Truth.assertThat(list.first().count()).isEqualTo(0)

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

        Truth.assertThat(list.first().count()).isEqualTo(1)

        coVerify(exactly = 1) { repository.getAllProducts(1L) }
        confirmVerified(repository)
    }

    @Test
    fun update_state_product() = runTest {
        coEvery { repository.changeActivatedProduct(id = 1L, activated = true)} returns Unit

        useCase.updateActivatedProduct(1L, true)

        coVerify(exactly = 1) { repository.changeActivatedProduct(1L, activated = true) }
        confirmVerified(repository)
    }
}