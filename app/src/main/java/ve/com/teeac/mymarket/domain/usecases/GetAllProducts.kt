package ve.com.teeac.mymarket.domain.usecases

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class GetAllProducts(
    private val repository: DetailMarketRepository
) {
   operator fun invoke(idMarket: Long): Flow<List<MarketDetail>> {
       return repository.getAllProducts(idMarket)
   }
}