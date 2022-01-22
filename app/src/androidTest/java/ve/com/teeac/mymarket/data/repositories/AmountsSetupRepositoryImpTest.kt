package ve.com.teeac.mymarket.data.repositories

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class AmountsSetupRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AmountsSetupRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun add_new_Amounts()= runTest {
        assertThat(repository.addAmounts(AmountsSetup())).isNotNull()
    }

    @Test
    fun get_Amounts_existing()= runTest {
        val setup = AmountsSetup(
            rate = 5.0,
            maximumAvailableDollar = 5.0,
            maximumAvailableBolivares = 25.0,
            marketId = 1L
        )
        val id = repository.addAmounts(setup)
        val addedSetup = repository.getAmounts(setup.marketId!!)
        assertThat(addedSetup).isEqualTo(setup.copy(id = id))
    }

    @Test
    fun get_Amounts_does_not_exist()= runTest {
        try {
            repository.getAmounts(1000L)
        }catch (e: InvalidPropertyApp){
            assertThat(e.message).isEqualTo(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST)
        }
    }

    @Test
    fun delete_amounts_setup_existing() = runTest {
        val setup = AmountsSetup(
            rate = 5.0,
            maximumAvailableDollar = 5.0,
            maximumAvailableBolivares = 25.0,
            marketId = 1L
        )
        val id = repository.addAmounts(setup)
        val addedSetup = repository.getAmounts(setup.marketId!!)
        assertThat(addedSetup).isNotNull()

        repository.deleteAmounts(id)

        try {
            repository.getAmounts(setup.marketId!!)
        }catch (e: InvalidPropertyApp){
            assertThat(e.message).isEqualTo(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST)
        }

    }

    @Test
    fun delete_amounts_setup_existing_By_MarketId() = runTest {
        val setup = AmountsSetup(
            rate = 5.0,
            maximumAvailableDollar = 5.0,
            maximumAvailableBolivares = 25.0,
            marketId = 1L
        )
        val setup2 = AmountsSetup(
            rate = 3.0,
            maximumAvailableDollar = 3.0,
            maximumAvailableBolivares = 9.0,
            marketId = 1L
        )
        repository.addAmounts(setup)
        repository.addAmounts(setup2)
        val addedSetup = repository.getAmounts(setup.marketId!!)
        assertThat(addedSetup).isNotNull()

        repository.deleteAmountsByMarketId(setup.marketId!!)

        try {
            repository.getAmounts(setup.marketId!!)
        }catch (e: InvalidPropertyApp){
            assertThat(e.message).isEqualTo(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST)
        }

    }
}