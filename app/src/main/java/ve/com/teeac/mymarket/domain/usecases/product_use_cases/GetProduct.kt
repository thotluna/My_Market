package ve.com.teeac.mymarket.domain.usecases.product_use_cases

import ve.com.teeac.mymarket.data.data_source.DetailMarketDao
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class GetProduct(
    private val repository: DetailMarketRepository
) {
    suspend operator fun invoke(id: Long): MarketDetail? {
        return repository.getProduct(id)
    }
}