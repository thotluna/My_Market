package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ve.com.teeac.mymarket.domain.usecases.DetailsMarketUseCase
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupController
import javax.inject.Inject

@HiltViewModel
class DetailsMarketViewModel @Inject constructor(
    private val useCase: DetailsMarketUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val stateMarketId = mutableStateOf(0L)

    val statesAmountsSetup = AmountSetupController()

    fun onEvent(event: DetailsMarketEvent) {
        when (event) {

            is DetailsMarketEvent.SaveAmountSetup -> {
                if (statesAmountsSetup.rate.value.number != null) {
                    viewModelScope.launch {
                        useCase.addAmountsSetup(
                            statesAmountsSetup.getAmountSetup(stateMarketId.value)
                        )
                    }
                }
            }
        }
    }

}