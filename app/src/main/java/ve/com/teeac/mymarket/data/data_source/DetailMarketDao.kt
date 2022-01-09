package ve.com.teeac.mymarket.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail

@Dao
interface DetailMarketDao {

    @Query("SELECT * FROM marketdetail WHERE marketId = :marketId")
    fun getAllDetailMarket(marketId: Int): Flow<List<MarketDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetailMarket(marketDetail: MarketDetail)

    @Delete
    suspend fun deleteDetailMarket(marketDetail: MarketDetail)
}