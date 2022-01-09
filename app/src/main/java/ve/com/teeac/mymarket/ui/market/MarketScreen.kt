package ve.com.teeac.mymarket.ui.market

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.ui.market.components.*

@Preview
@Composable
fun MarketScreen() {
    val listProducts = remember { mutableStateOf(getDetail()) }
    val amountsSetup = remember { mutableStateOf(value = AmountsSetup(5.2, 5.2, 50.0, 1L)) }

    MyMarketApp {
        ScaffoldApp(topBar = { TopBarMarket() }) {
            Amounts(amountsSetup.value)
            FormProduct()
            ListProductsCard(listProducts.value)
            Totals(
                amountDollar = listProducts.value.sumOf { it.amountDollar },
                amount = listProducts.value.sumOf { it.amount },
                amountsSetup = amountsSetup.value
            )
        }
    }
}

fun getDetail() = (1..50).map {
    val converter = 5.2
    val quantity = it / 3.00
    MarketDetail(
        marketId = 1,
        id = it,
        quantity = quantity,
        description = "Product number $it",
        unitAmountDollar = it * 0.42,
        unitAmount = (it * 0.42) * converter,
        amountDollar = (it * 0.42) * quantity,
        amount = (it * 0.42) * 5.2 * quantity
    )
}