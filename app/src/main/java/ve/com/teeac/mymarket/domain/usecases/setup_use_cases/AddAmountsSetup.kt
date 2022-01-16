package ve.com.teeac.mymarket.domain.usecases.setup_use_cases

import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository

class AddAmountsSetup(
    private val repository: AmountsSetupRepository
) {
    suspend operator fun invoke(amounts: AmountsSetup): Long {
        return repository.addAmounts(amounts)
    }

}