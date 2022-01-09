package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository

class AddMarket(
    private val repository: MarketsRepository
) {

    suspend operator fun invoke(market: Market): Long{
        return repository.addMarket(market)
    }
}