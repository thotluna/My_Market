package ve.com.teeac.mymarket.domain.usecases

data class DetailsMarketUseCase(

    val addAmountsSetup: AddAmountsSetup,
    val getAmountsSetup: GetAmountsSetup,

    val addMarket: AddMarket,

    val getAllProducts: GetAllProducts,
    val addProduct: AddProduct,
    val getProduct: GetProduct,
    val updateProduct: UpdateProductsMarketUseCase,
    val deleteProduct: DeleteProduct
)
