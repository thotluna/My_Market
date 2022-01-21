package ve.com.teeac.mymarket.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.MarketDetail

@Dao
interface DetailMarketDao {

    @Query("SELECT * FROM marketdetail WHERE marketId = :marketId")
    fun getAllDetailMarket(marketId: Long): Flow<List<MarketDetail>>

    @Query("SELECT * FROM marketdetail WHERE id = :id")
    suspend fun getDetailMarket(id: Long): MarketDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetailMarket(marketDetail: MarketDetail)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addListDetailMarket(list: List<MarketDetail>)

    @Query("DELETE FROM marketdetail WHERE id = :id")
    suspend fun deleteDetailMarket(id: Long)

    @Query("UPDATE marketdetail SET isActive = :activated WHERE id = :id")
    suspend fun changeActivated(id: Long, activated: Boolean)
}