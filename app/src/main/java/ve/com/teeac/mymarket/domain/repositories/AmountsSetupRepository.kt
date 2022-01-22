package ve.com.teeac.mymarket.domain.repositories

import ve.com.teeac.mymarket.domain.model.AmountsSetup

interface AmountsSetupRepository{

    /**
     * add new AmountSetup by database, if it exists then update it
     *
     * @param amounts entity AmountSetup
     * @return type: Long  -  id entity
     */
    suspend fun addAmounts(amounts: AmountsSetup): Long

    /**
     * returns an AmountSetup from the database that has the requested Market.
     * if it does not exist it generates an exception InvalidPropertyApp
     *
     * @param marketId Long AmountSetup
     * @return type: AmountsSetup
     * @throws Exception if does not exist
     */
    suspend fun getAmounts(marketId: Long): AmountsSetup

    /**
     * delete one AmountsSetup by id.
     * if it does not exist, nothing happens
     *
     * @param id Long AmountSetup
     */
    suspend fun deleteAmounts(id: Long)

    /**
     * delete all AmountsSetup by marketId.
     * if it does not exist, nothing happens
     *
     * @param marketId Long id market
     */
    suspend fun deleteAmountsByMarketId(marketId: Long)
}