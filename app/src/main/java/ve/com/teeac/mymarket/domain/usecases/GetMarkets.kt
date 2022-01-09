package ve.com.teeac.mymarket.domain.usecases

import kotlinx.coroutines.flow.Flow
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.repositories.MarketsRepository

class GetMarkets(
    private val repository: MarketsRepository
    ) {

    operator fun invoke(): Flow<List<Market>> {
        return repository.getAllMarkers()
    }
}