package ve.com.teeac.mymarket.domain.usecases

import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.AddAmountsSetup
import ve.com.teeac.mymarket.domain.usecases.setup_use_cases.GetAmountsSetup

data class SetupUseCase(
    val addSetup: AddAmountsSetup,
    val getSetup: GetAmountsSetup
)
