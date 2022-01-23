package ve.com.teeac.mymarket.domain.usecases.setup_use_cases

import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository

class GetAmountsSetup(
    private val repository: AmountsSetupRepository
) {
    /**
     * @param marketsId
     * @return AmountsSetup?
     */
    suspend operator fun invoke(marketsId: Long): AmountsSetup {
        return repository.getAmounts(marketsId)
    }
}