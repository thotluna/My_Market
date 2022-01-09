package ve.com.teeac.mymarket.domain.repositories

import ve.com.teeac.mymarket.domain.model.AmountsSetup

interface AmountsSetupRepository{

    suspend fun addAmounts(amounts: AmountsSetup)
    suspend fun getAmounts(marketId: Int): AmountsSetup
}