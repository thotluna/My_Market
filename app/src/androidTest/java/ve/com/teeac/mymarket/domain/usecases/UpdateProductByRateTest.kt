package ve.com.teeac.mymarket.domain.usecases

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ve.com.teeac.mymarket.di.AppModule
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.model.MarketDetail
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(AppModule::class)
@SmallTest
@RunWith(AndroidJUnit4::class)
class UpdateProductByRateTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var marketUseCase: MarketUseCases

    @Inject
    lateinit var useCaseSetup: SetupUseCase

    @Inject
    lateinit var useCaseProduct: ProductUseCase

    private val productWithoutAmounts = MarketDetail(
        id = 3,
        marketId = 1,
        quantity = 4.0,
        description = "Papas",
        unitAmountDollar = 0.0,
        amountDollar = 0.0
    )


    private val productBs = MarketDetail(
        id = 2,
        marketId = 1,
        quantity = 2.0,
        description = "Papas",
        unitAmount = 30.5,
        amount = 61.0
    )

    private val productDollar = MarketDetail(
        id = 1,
        marketId = 1,
        quantity = 1.0,
        description = "Papas",
        unitAmountDollar = 2.3,
        amountDollar = 2.3
    )

    @Before
    fun setUp()= runTest {
        hiltRule.inject()

        marketUseCase.addMarket(Market(
            date = System.currentTimeMillis(),
            id = 1,
            amount = 50.0,
            amountDollar = 10.0
        ))
        useCaseSetup.addSetup(AmountsSetup(
            id = 1,
            marketId = 1,
            rate = 5.0,
        ))
        useCaseProduct.addProduct(productDollar)
        useCaseProduct.addProduct(productBs)
        useCaseProduct.addProduct(productWithoutAmounts)
    }

    @Test
    fun productWithoutAmountsTest_Nothing() = runTest{

        useCaseProduct.updateProducts(
            rate = 5.0,
            marketId = 1
        )

        val product = useCaseProduct.getProduct(productWithoutAmounts.id!!)

        assertThat(product).isEqualTo(productWithoutAmounts)

    }

    @Test
    fun productAmountsBsTest_ChangeAmountDollar() = runTest{

        useCaseProduct.updateProducts(
            rate = 5.0,
            marketId = 1
        )

        val product = useCaseProduct.getProduct(productBs.id!!)

        assertThat(product).isNotEqualTo(productBs)

        assertThat(product!!.amountDollar).isEqualTo(productBs.amount / 5.0)
        assertThat(product.unitAmountDollar).isEqualTo(productBs.unitAmount / 5.0)

    }

    @Test
    fun productAmountsDollarTest_ChangeAmount() = runTest{

        useCaseProduct.updateProducts(
            rate = 5.0,
            marketId = 1
        )

        val product = useCaseProduct.getProduct(productDollar.id!!)

        assertThat(product).isNotEqualTo(productDollar)

        assertThat(product!!.amount).isEqualTo(productDollar.amountDollar * 5.0)
        assertThat(product.unitAmount).isEqualTo(productDollar.unitAmountDollar * 5.0)

    }


}