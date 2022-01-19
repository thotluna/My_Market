package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.usecases.product_use_cases.*
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.AddAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.GetAmountsSetup

data class DetailsMarketUseCase(

    val addAmountsSetup: AddAmountsSetup,
    val getAmountsSetup: GetAmountsSetup,

    val addMarket: AddMarket,
    val getAllProducts: GetAllProducts,
    val addProduct: AddProduct,
    val getProduct: GetProduct,
    val updateProduct: UpdateProductByRate,
    val deleteProduct: DeleteProduct
)
