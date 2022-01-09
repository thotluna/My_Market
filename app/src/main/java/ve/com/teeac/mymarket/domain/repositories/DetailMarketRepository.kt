package ve.com.teeac.mymarket.domain.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail

interface DetailMarketRepository {

    fun getAllProducts(marketId: Int): Flow<List<MarketDetail>>

    suspend fun getProduct(id: Int): MarketDetail

    suspend fun addProduct(product: MarketDetail)

    suspend fun deleteProduct(product: MarketDetail)
}