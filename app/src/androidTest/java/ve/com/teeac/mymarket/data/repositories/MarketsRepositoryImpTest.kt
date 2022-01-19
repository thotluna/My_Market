package ve.com.teeac.mymarket.data.repositories

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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
import org.junit.runner.RunWith
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
@SmallTest
@RunWith(AndroidJUnit4::class)
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
    fun testGetAllMarkersEmpty() = runTest{
        val list = repository.getAllMarkers().first()
        assertThat(list.isEmpty()).isTrue()
    }


    @Test
    fun testGetAllMarkers() = runTest{
        populateMarkets()
        val list = repository.getAllMarkers().first()
        assertThat(list.size).isEqualTo(50)
    }

    @Test
    fun testAddMarket() = runTest {
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
    fun getMarket()= runTest {
        val market = Market(
            date = System.currentTimeMillis(),
            amountDollar = 2.5,
            amount = 100.0
        )

        val idMarket = repository.addMarket(market)

        val marketDb = repository.getMarker(idMarket)

        assertThat(marketDb).isEqualTo(
            market.copy(
                id = idMarket
            )
        )
    }

    fun testDeleteMarket() = runTest {
        val market = Market(
            id = 51,
            date = System.currentTimeMillis(),
            amountDollar = 2.5,
            amount = 100.0
        )
        repository.addMarket(market)
        repository.deleteMarket(market.id!!)
        val listNew = repository.getAllMarkers().first()
        assertThat(listNew.isEmpty()).isTrue()
    }

    private fun populateMarkets() = runTest {
        (1L..50).map {
            val converter = 5.2
            val calAmountDollar: Double = 2.0 * it.toDouble()
            repository.addMarket(Market(
                id = it,
                date = System.currentTimeMillis()-(86400000 * it),
                amountDollar = calAmountDollar,
                amount = calAmountDollar * converter
            ))
        }
    }
}