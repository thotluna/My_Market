package ve.com.teeac.mymarket.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.model.MarketDetail

@Database(entities = [Market::class, MarketDetail::class, AmountsSetup::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract val marketDao: MarketDao
    abstract val detailMarketDao: DetailMarketDao
    abstract val amountsSetupDao: AmountsSetupDao

    companion object {
        const val DATABASE_NAME = "markets_data"
    }
}