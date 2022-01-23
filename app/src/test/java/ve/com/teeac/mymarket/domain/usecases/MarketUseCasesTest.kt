package ve.com.teeac.mymarket.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.DeleteMarket
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.GetMarkets

@ExperimentalCoroutinesApi
class MarketUseCasesTest {

    @MockK
    lateinit var addMarket: AddMarket
    @MockK
    lateinit var getMarkets: GetMarkets
    @MockK
    lateinit var repository: MarketsRepository
    @MockK
    lateinit var productRepository: DetailMarketRepository
    @MockK
    lateinit var setupRepository: AmountsSetupRepository

    private lateinit var deleteUseCase: DeleteMarket

    lateinit var useCase: MarketUseCases

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)

        deleteUseCase = DeleteMarket(repository, setupRepository, productRepository)

        useCase = MarketUseCases( addMarket,getMarkets,deleteUseCase)
    }

    @Test
    fun add_new_Market() = runTest {
        val slot = slot<Market>()
        coEvery { addMarket(capture(slot)) }returns(1L)

        useCase.addMarket(Market())

        assertThat(slot.captured.copy(date = 0L)).isEqualTo(Market( date = 0L))

        coVerify(exactly = 1){ addMarket(any())}
        confirmVerified(addMarket)
    }

    @Test
    fun getMarket_exist()= runTest{
        coEvery { getMarkets()}.returns( flow { emit(listOf(Market(id = 1L))) } )

        val res = useCase.getMarkets().first().first()

        assertThat(res.copy(date = 0L)).isEqualTo(Market(id=1L, date = 0L))

        coVerify(exactly = 1){ getMarkets()}
        confirmVerified(getMarkets)
    }

    @Test
    fun getMarket_empty_list()= runTest{
        coEvery { getMarkets()}.returns( flow { emit(emptyList<Market>()) } )

        assertThat(useCase.getMarkets().first().count()).isEqualTo(0)

        coVerify(exactly = 1){ getMarkets()}
        confirmVerified(getMarkets)
    }

    @Test
    fun delete_Market() = runTest{
        val marketId = 1L
        coEvery { setupRepository.deleteAmountsByMarketId(marketId)} returns Unit
        coEvery { productRepository.deleteProducts(marketId)} returns Unit
        coEvery { repository.deleteMarket(marketId)} returns Unit
        
        useCase.deleteMarket(marketId)

        coVerify(exactly = 1){ setupRepository.deleteAmountsByMarketId(marketId)}
        coVerify(exactly = 1){ productRepository.deleteProducts(marketId)}
        coVerify(exactly = 1){ repository.deleteMarket(marketId)}
        confirmVerified(setupRepository)
        confirmVerified(productRepository)
        confirmVerified(repository)
    }

}