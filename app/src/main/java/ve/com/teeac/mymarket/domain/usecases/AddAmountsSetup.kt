package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.repositories.AmountsSetupRepository

class AddAmountsSetup(
    private val repository: AmountsSetupRepository
) {
    suspend operator fun invoke(amounts: AmountsSetup) {
        repository.addAmounts(amounts)
    }

}