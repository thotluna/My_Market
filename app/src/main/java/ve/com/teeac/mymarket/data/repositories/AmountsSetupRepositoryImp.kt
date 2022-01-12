package ve.com.teeac.mymarket.data.repositories

import ve.com.teeac.mymarket.data.data_source.AmountsSetupDao
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository

class AmountsSetupRepositoryImp(
    private val dao: AmountsSetupDao
) : AmountsSetupRepository{
    override suspend fun addAmounts(amounts: AmountsSetup) {
        dao.insertAmounts(amounts)
    }

    override suspend fun getAmounts(marketId: Long): AmountsSetup? {
        return dao.getAmountsSetup(marketId)
    }
}