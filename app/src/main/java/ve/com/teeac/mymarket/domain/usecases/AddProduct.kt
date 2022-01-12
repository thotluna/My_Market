package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.data.data_source.DetailMarketDao
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class AddProduct(
    private val repository: DetailMarketRepository
) {

    suspend operator fun invoke(product: MarketDetail) {
        repository.addProduct(product)
    }
}