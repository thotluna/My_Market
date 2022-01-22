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
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
class DetailsMarketRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: DetailMarketRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        populateProducts()
    }

    @Test
    fun getAll_Products_emptyList() = runTest {
        assertThat(repository.getAllProducts(1000L).first())
            .isEqualTo(emptyList<MarketDetail>())
    }

    @Test
    fun getAll_Products_fullList() = runTest {
        assertThat(repository.getAllProducts(5L).first())
            .isEqualTo(listOf(createProduct(5L)))
    }

    @Test
    fun get_Product_existing() = runTest {
        val productDb = repository.getProduct(5L)
        assertThat(productDb).isEqualTo(createProduct(5L))
    }

    @Test
    fun get_Product_does_not_exist() = runTest {
        try {
            repository.getProduct(5000L)
        } catch (e: InvalidPropertyApp) {
            assertThat(e.message).isEqualTo(InvalidPropertyApp.PRODUCT_DOES_NOT_EXIST)
        }
    }

    @Test
    fun add_new_Product() = runTest {
        val numDatabase = repository.getAllProducts(1L).first().count()
        val id = repository.addProduct(createProduct(1L).copy(id = 55L))
        assertThat(id).isEqualTo(55L)
        assertThat(repository.getAllProducts(1L).first().count())
            .isEqualTo(numDatabase + 1)
    }

    @Test
    fun update_existing_Product() = runTest {
        val expected = createProduct(2L).copy(unitAmountDollar = 100.00)
        val oldProduct = repository.getProduct(2L)
        repository.addProduct(expected)
        val updatingProduct = repository.getProduct(2L)

        assertThat(updatingProduct).isNotEqualTo(oldProduct)
        assertThat(updatingProduct).isEqualTo(expected)
    }

    @Test
    fun delete_Product() = runTest {
        val product = repository.getProduct(6L)
        assertThat(product).isNotNull()
        repository.deleteProduct(product!!.id!!)
        try {
            repository.getProduct(product.id!!)
        } catch (e: InvalidPropertyApp) {
            assertThat(e.message).isEqualTo(InvalidPropertyApp.PRODUCT_DOES_NOT_EXIST)
        }
    }

    @Test
    fun delete_Product_by_marketId() = runTest {
        val productNew = MarketDetail(
            id = 60L,
            marketId = 6L,
            quantity = 3.0,
            unitAmountDollar = 100.00,
            amountDollar = 300.00,
            description = "Product 60",
            isActive = true
        )
        repository.addProduct(productNew)
        val products = repository.getAllProducts(6L).first()
        val count = products.count()
        assertThat(count).isEqualTo(2)
        repository.deleteProducts(6L)
        assertThat(repository.getAllProducts(6L).first().count()).isEqualTo(0)
    }

    @Test
    fun update_Product_ByRate() = runTest {
        val product = MarketDetail(
            id = 60L,
            marketId = 60L,
            quantity = 3.0,
            unitAmountDollar = 100.00,
            amountDollar = 300.00,
            description = "Product 60",
            isActive = true
        )

        val rate = 5.0

        val expected = product.copy(
            unitAmount = 500.0,
            amount = 1500.0
        )
        repository.addProduct(product)

        repository.addListProduct(
            repository.getAllProducts(product.marketId).first().map{
                it.addRate(rate)
            }
        )

        val db = repository.getAllProducts(product.marketId).first()

        assertThat(db).isEqualTo(listOf(expected))

    }

    @Test
    fun changeActivated_Product() = runTest {
        assertThat(repository.getProduct(11L)!!.isActive).isFalse()
        repository.changeActivatedProduct(11L, activated = true)
        assertThat(repository.getProduct(11L)!!.isActive).isTrue()
        repository.changeActivatedProduct(11L, activated = false)
        assertThat(repository.getProduct(11L)!!.isActive).isFalse()
    }

    private fun populateProducts() = runTest {
        (1L..50).map {
            repository.addProduct(createProduct(it))
        }
    }

    private fun createProduct(id: Long): MarketDetail {
        val converter = 5.2
        val calAmountDollar: Double = 2.0 * id.toDouble()

        return MarketDetail(
            id = id,
            marketId = id,
            quantity = 3.0,
            unitAmountDollar = calAmountDollar,
            unitAmount = calAmountDollar * converter,
            amountDollar = calAmountDollar * 3,
            amount = calAmountDollar * converter * 3,
            description = "Product $id",
            isActive = id.toInt() % 2 == 0
        )
    }
}