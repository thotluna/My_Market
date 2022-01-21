package ve.com.teeac.mymarket.data.repositories

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.data.data_source.DetailMarketDao
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository

class DetailsMarketRepositoryImp(
    private val dao: DetailMarketDao
): DetailMarketRepository {

    override fun getAllProducts(marketId: Long): Flow<List<MarketDetail>> {
        return dao.getAllDetailMarket(marketId)
    }

    override suspend fun getProduct(id: Long): MarketDetail? {
        return dao.getDetailMarket(id)
    }

    override suspend fun addProduct(product: MarketDetail) {
        dao.addDetailMarket(product)
    }

    override suspend fun deleteProduct(id: Long) {
        dao.deleteDetailMarket(id)
    }

    override suspend fun updateProductByRate(list: List<MarketDetail>) {
        dao.addListDetailMarket(list)
    }

    override suspend fun changeActivatedProduct(id: Long, activated: Boolean) {
        dao.changeActivated(id, activated)
    }
}