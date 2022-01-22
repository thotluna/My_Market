package ve.com.teeac.mymarket.domain.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.Market

interface MarketsRepository {

    /**
     * return all market
     */
    fun getAllMarkers(): Flow<List<Market>>

    /**
     * return Market from db.
     * if it does not exist it generated an Exception InvalidatePropertyApp
     *
     * @param marketId
     * @return Market
     * @throws Exception if does not exist
     */
    suspend fun getMarker(marketId: Long): Market

    /**
     * add new market or updating if it existed
     *
     * @param market
     * @return id
     */
    suspend fun addMarket(market: Market): Long

    /**
     * delete market the db
     * if it does not exist, nothing happens
     *
     * @param id
     */
    suspend fun deleteMarket(id: Long)

}