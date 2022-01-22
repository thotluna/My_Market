package ve.com.teeac.mymarket.data.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.data.data_source.MarketDao
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp

class MarketsRepositoryImp(
    private val dao: MarketDao
) : MarketsRepository {

    override fun getAllMarkers(): Flow<List<Market>> {
        return dao.getMarkets()
    }

    override suspend fun getMarker(marketId: Long): Market {

        return dao.getMarket(marketId)
            ?: throw InvalidPropertyApp(InvalidPropertyApp.MARKET_DOES_NOT_EXIST)

    }

    override suspend fun addMarket(market: Market): Long {
        return dao.addMarket(market)
    }

    override suspend fun deleteMarket(id: Long) {
        dao.delMarket(id)
    }

}