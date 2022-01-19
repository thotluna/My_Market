package ve.com.teeac.mymarket.domain.usecases.product_use_cases

import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class AddProduct(
    private val repository: DetailMarketRepository
) {

    suspend operator fun invoke(product: MarketDetail) {
        repository.addProduct(product)
    }
}