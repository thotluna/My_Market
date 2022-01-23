package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.usecases.market_use_cases.AddMarket
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.*

data class DetailsMarketUseCase(
    val addMarket: AddMarket,
    val getAllProducts: GetAllProducts,
    val updateActivatedProduct: ActivatedProduct
)
