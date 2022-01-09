package ve.com.teeac.mymarket.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ve.com.teeac.mymarket.data.MarketRepository
import ve.com.teeac.mymarket.domain.model.Market
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MarketRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val  _listMarket = mutableStateOf<List<Market>>(listOf())
    val listMarket: State<List<Market>>
        get() = _listMarket

    init{
        _listMarket.value = repository.getMarkets()
    }

}