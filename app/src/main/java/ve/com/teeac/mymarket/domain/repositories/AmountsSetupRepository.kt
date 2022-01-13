package ve.com.teeac.mymarket.domain.repositories

import ve.com.teeac.mymarket.domain.model.AmountsSetup

interface AmountsSetupRepository{

    suspend fun addAmounts(amounts: AmountsSetup): Long
    suspend fun getAmounts(marketId: Long): AmountsSetup?
}