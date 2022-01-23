package ve.com.teeac.mymarket.presentation.markets

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After

import org.junit.Before
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.GetMarkets
import ve.com.teeac.mymarket.domain.usecases.MarketUseCases
import ve.com.teeac.mymarket.domain.usecases.market_use_cases.DeleteMarket


@ExperimentalCoroutinesApi
class MarketsViewModelTest {

//    private val standardTestDispatcher = StandardTestDispatcher()

    @MockK
    lateinit var addMarkets: AddMarket

    @MockK
    lateinit var getMarkets: GetMarkets

    @MockK
    lateinit var deleteMarket: DeleteMarket

    private var listMarkets = listOf(
        Market(
            id = 1,
            date = System.currentTimeMillis(),
            amountDollar = 50.0,
            amount = 150.0
        ),
        Market(
            id = 2,
            date = System.currentTimeMillis() + 3600000,
            amountDollar = 45.0,
            amount = 130.0
        ),
        Market(
            id = 2,
            date = System.currentTimeMillis() + 3600000 + 3600000,
            amountDollar = 30.0,
            amount = 90.0
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @After
    fun tearDown() {
//        Dispatchers.resetMain()
    }

    @Test
    fun initialViewModel(){
        val useCases = MarketUseCases(addMarkets, getMarkets, deleteMarket)

        every { useCases.getMarkets() } answers {
            flow {
                emit(listMarkets)
            }

        }


        runTest{
            val viewModel = MarketsViewModel(
                useCase = useCases,
            )
            /*TODO: delete delay*/
            delay(500L)
            assertThat(viewModel.state.value.markets).isEqualTo(listMarkets)
        }


    }


}