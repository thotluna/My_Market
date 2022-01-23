package ve.com.teeac.mymarket.domain.usecases.market_use_cases

import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository
import ve.com.teeac.mymarket.domain.repositories.DetailMarketRepository
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository

class DeleteMarket (
    private val repository: MarketsRepository,
    private val repositorySetup: AmountsSetupRepository,
    private val repositoryDetailMarket: DetailMarketRepository
){
    suspend operator fun invoke(idMarket: Long){
        repositoryDetailMarket.deleteProducts(idMarket)
        repositorySetup.deleteAmountsByMarketId(idMarket)
        repository.deleteMarket(idMarket)
    }


}