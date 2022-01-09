package ve.com.teeac.mymarket.presentation.markets

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.MarketUseCases
import javax.inject.Inject

@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val useCase: MarketUseCases
): ViewModel() {

    private val _state = mutableStateOf(MarketsState())
    val state: State<MarketsState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    private var getMarketsJob: Job? = null

    init {
        getMarkets()
    }

    fun onEvent(event: MarketsEvent){
        when(event){
            is MarketsEvent.SaveMarket -> {
                val market = Market()
                viewModelScope.launch(Dispatchers.IO) {
                    val idMarket = useCase.addMarket(market)
                    _eventFlow.emit(UiEvent.NavigateToDetails(idMarket))
                }
            }
        }
    }

    private fun getMarkets(){
        getMarketsJob?.cancel()
        getMarketsJob = useCase.getMarkets()
            .onEach { markets ->
                _state.value = state.value.copy(
                    markets = markets
                )
            }.launchIn(viewModelScope)

    }


}