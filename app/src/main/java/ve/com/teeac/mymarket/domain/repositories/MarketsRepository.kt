package ve.com.teeac.mymarket.domain.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.Market

interface MarketsRepository {

    fun getAllMarkers(): Flow<List<Market>>

    suspend fun getMarker(marketId: Long): Market?

    suspend fun addMarket(market: Market): Long

    suspend fun deleteMarket(id: Long)

}