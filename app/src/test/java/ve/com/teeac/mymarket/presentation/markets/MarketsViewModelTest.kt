package ve.com.teeac.mymarket.presentation.markets

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.GetMarkets
import ve.com.teeac.mymarket.domain.usecases.MarketUseCases
import ve.com.teeac.mymarket.utils.MainCoroutineRule


@ExperimentalCoroutinesApi
class MarketsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var addMarkets: AddMarket

    @MockK
    lateinit var getMarkets: GetMarkets

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
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun initialViewModel() = runTest {
        val useCases = MarketUseCases(addMarkets, getMarkets)

        coEvery { useCases.getMarkets()}coAnswers {
            flow {
                emit(listMarkets)
            }
        }

        val viewModel = MarketsViewModel(useCases)
        assertThat(viewModel.state.value.markets).isEqualTo(listMarkets)
    }


}