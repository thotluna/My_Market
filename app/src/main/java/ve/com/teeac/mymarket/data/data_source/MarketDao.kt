package ve.com.teeac.mymarket.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.Market

@Dao
interface MarketDao {

    @Query("SELECT * FROM market")
    fun getMarkets(): Flow<List<Market>>

    @Query("SELECT * FROM market WHERE id = :marketId")
    suspend fun getMarket(marketId: Long): Market?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMarket(market: Market): Long

    @Query("DELETE FROM market WHERE id = :id")
    suspend fun delMarket(id: Long)

}