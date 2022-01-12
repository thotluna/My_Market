package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository

class GetAmountsSetup(
    private val repository: AmountsSetupRepository
) {
    suspend operator fun invoke(marketsId: Long): AmountsSetup? {
        return repository.getAmounts(marketsId)
    }
}