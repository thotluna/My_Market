package ve.com.teeac.mymarket.domain.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail

interface DetailMarketRepository {

    fun getAllProducts(marketId: Long): Flow<List<MarketDetail>>

    suspend fun getProduct(id: Long): MarketDetail?

    suspend fun addProduct(product: MarketDetail)

    suspend fun deleteProduct(id: Long)

    suspend fun updateProductByRate(list: List<MarketDetail>)

    suspend fun changeActivatedProduct(id: Long, activated: Boolean)
}