package ve.com.teeac.mymarket.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ve.com.teeac.mymarket.domain.model.AmountsSetup

@Dao
interface AmountsSetupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAmounts(amounts: AmountsSetup)

    @Query("SELECT * FROM amountssetup WHERE marketId = :marketId")
    suspend fun getAmountsSetup(marketId: Int): AmountsSetup

}