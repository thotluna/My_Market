package ve.com.teeac.mymarket.domain.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail

interface DetailMarketRepository {

    /**
     * return all MarketDetail with marketId
     *
     * @param marketId market id
     * @return Flow<List<MarketDetail>>
     */
    fun getAllProducts(marketId: Long): Flow<List<MarketDetail>>

    /**
     * return MarketDetail from db.
     * if it does not exist it generated an Exception InvalidatePropertyApp
     *
     * @param id
     * @return MarketDetail
     * @throws Exception if does not exist
     */
    suspend fun getProduct(id: Long): MarketDetail?

    /**
     * add new MarketDetail or updating if it existed
     *
     * @param product
     * @return id
     */
    suspend fun addProduct(product: MarketDetail): Long

    /**
     * delete MarketDetail the db
     * if it does not exist, nothing happens
     *
     * @param id
     */
    suspend fun deleteProduct(id: Long)

    /**
     * delete all MarketDetail the db where are equals to market id
     * if it does not exist, nothing happens
     *
     * @param idMarket
     */
    suspend fun deleteProducts(idMarket: Long)

    /**
     * add list MarketDetail
     *
     * @param list  List<MarketDetail>
     */
    suspend fun addListProduct(list: List<MarketDetail>)

    /**
     * change state of MarketDetail
     *
     * @param id
     * @param activated
     */
    suspend fun changeActivatedProduct(id: Long, activated: Boolean)
}