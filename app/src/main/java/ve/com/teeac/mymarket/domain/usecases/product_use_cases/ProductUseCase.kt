package ve.com.teeac.mymarket.domain.usecases.product_use_cases

data class ProductUseCase(
    val addProduct: AddProduct,
    val getAllProducts: GetAllProducts,
    val getProduct: GetProduct,
    val deleteProduct: DeleteProduct,
    val updateProducts: UpdateProductByRate
)
