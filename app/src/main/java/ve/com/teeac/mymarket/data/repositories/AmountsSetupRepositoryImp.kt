package ve.com.teeac.mymarket.data.repositories

import ve.com.teeac.mymarket.data.data_source.AmountsSetupDao
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp

class AmountsSetupRepositoryImp(
    private val dao: AmountsSetupDao
) : AmountsSetupRepository{
    override suspend fun addAmounts(amounts: AmountsSetup): Long {
        return dao.insertAmounts(amounts)
    }

    override suspend fun getAmounts(marketId: Long): AmountsSetup {
        return dao.getAmountsSetup(marketId)
            ?: throw InvalidPropertyApp(InvalidPropertyApp.AMOUNTS_SETUP_DOES_NOT_EXIST)
    }

    override suspend fun deleteAmounts(id: Long) {
        return dao.delete(id)
    }

    override suspend fun deleteAmountsByMarketId(marketId: Long) {
        return dao.deleteByMarketId(marketId)
    }


}