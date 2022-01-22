package ve.com.teeac.mymarket.data.repositories

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class MarketsRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: MarketsRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun get_AllMarket_With_EmptyList() = runTest {
        val list = repository.getAllMarkers().first()
        assertThat(list.isEmpty()).isTrue()
    }

    @Test
    fun get_AllMarket_With_FullList() = runTest {
        populateMarkets()
        val list = repository.getAllMarkers().first()
        assertThat(list.size).isEqualTo(50)
    }

    @Test
    fun get_Market_Existing() = runTest {

        populateMarkets()

        val market = createMarket(5L)
        val marketDb = repository.getMarker(market.id!!)

        assertThat(marketDb).isNotNull()
        assertThat(marketDb.id).isEqualTo(market.id)
        assertThat(marketDb.amount).isEqualTo(market.amount)
        assertThat(marketDb.amountDollar).isEqualTo(market.amountDollar)
    }

    @Test
    fun get_Market_does_not_exist() = runTest {
        populateMarkets()
        val market = createMarket(1000)
        try {
            repository.getMarker(market.id!!)
        } catch (e: InvalidPropertyApp) {
            assertThat(e.message).isEqualTo(InvalidPropertyApp.MARKET_DOES_NOT_EXIST)
        }
    }

    @Test
    fun add_new_market() = runTest {
        val market = Market(
            date = System.currentTimeMillis(),
            amountDollar = 2.5,
            amount = 100.0
        )
        val listOld = repository.getAllMarkers().first()

        repository.addMarket(market)

        val listNew = repository.getAllMarkers().first()

        assertThat(listNew.size).isEqualTo(listOld.size + 1)
    }

    @Test
    fun add_market_update() = runTest {

        populateMarkets()

        val listOld = repository.getAllMarkers().first()

        val update = listOld.first().copy(
            amount = 1000.00
        )

        repository.addMarket(update)

        val listNew = repository.getAllMarkers().first()
        val updated = repository.getMarker(update.id!!)

        assertThat(listNew.size).isEqualTo(listOld.size )
        assertThat(updated).isEqualTo(update)
    }

    @Test
    fun delete_market_existing() = runTest {
        populateMarkets()
        val count = repository.getAllMarkers().first().count()
        repository.deleteMarket(5)
        val newCount = repository.getAllMarkers().first().count()

        assertThat(newCount).isEqualTo(count - 1)
    }

    @Test
    fun delete_market_does_not_existing() = runTest {
        populateMarkets()
        val count = repository.getAllMarkers().first().count()
        repository.deleteMarket(1000)
        val newCount = repository.getAllMarkers().first().count()

        assertThat(newCount).isEqualTo(count)
    }


    private fun populateMarkets() = runTest {
        (1L..50).map {
            repository.addMarket(createMarket(it))
        }
    }

    private fun createMarket(id: Long): Market {
        val converter = 5.2
        val calAmountDollar: Double = 2.0 * id.toDouble()

        return Market(
            id = id,
            date = System.currentTimeMillis() - (86400000 * id),
            amountDollar = calAmountDollar,
            amount = calAmountDollar * converter
        )
    }
}