package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.DetailsMarketUseCase
import ve.com.teeac.mymarket.presentation.InvalidEventException
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupEvent
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.SetupController
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductEvent
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductFormController
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.UiEvent
import javax.inject.Inject

@HiltViewModel
class DetailsMarketViewModel @Inject constructor(
    private val useCase: DetailsMarketUseCase,
    var setupController: SetupController,
    val productController: ProductFormController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var getNotesJob: Job? = null

    private val _state = mutableStateOf(DetailsMarketState(marketId = -1L))
    val state: State<DetailsMarketState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("marketId")?.let{
            if (it > -1) {
                savedStateHandle.get<Long?>("marketId")?.let { idMarket ->
                    loadIdMarket(idMarket)
                    viewModelScope.launch {
                        initialAmountSetup(idMarket)
                    }
                    getProductList()

                }
            }
        }


    }

    fun onEvent(event: DetailsMarketEvent) {
        when (event) {
            is DetailsMarketEvent.SaveAmountSetup -> {
                viewModelScope.launch {
                    val idMarket = getMarketId()
                    setupController.onEvent(AmountSetupEvent.Save(idMarket))
                    setupController.onToggleSection()
                }
                productController.onEvent(ProductEvent.UpdateRate(setupController.rate.value.number))
            }
            is DetailsMarketEvent.SaveProduct -> {
            viewModelScope.launch {
                try {
                    val idMarket = getMarketId()
                    productController.saveProduct(idMarket)



                } catch (e: InvalidPropertyApp) {
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(
                            message = e.message ?: "No se pudo guardar el nuevo producto"
                        )
                    )
                }
            }
        }
            is DetailsMarketEvent.UpdateProduct -> {
                viewModelScope.launch {
                    try {
                        productController.loadProduct(event.idProduct)
                    } catch (e: InvalidPropertyApp) {
                        _eventFlow.emit(
                            UiEvent
                                .ShowSnackBar(
                                    message = e.message ?: "No se pudo actualizar los productos"
                                )
                        )
                    }
                }

            }
            is DetailsMarketEvent.DeleteProduct -> {
                viewModelScope.launch {
                    productController.deleteProduct(event.idProduct)
                }
            }
            is DetailsMarketEvent.ClearProductForm -> {
                productController.clear()
            }
            else -> throw InvalidEventException(InvalidEventException.DETAILS_MARKET_EVENT)
        }
    }

    private suspend fun getMarketId(): Long {

        state.value.marketId?.let { marketId ->
            return if (marketId > -1) {
                state.value.marketId!!
            } else {
                createMarket()
            }
        } ?: return createMarket()

    }

    private suspend fun createMarket(): Long {
        return useCase.addMarket(Market()).also {
            _state.value = state.value.copy(
                marketId = it
            )
            productController.loadIdMarket(it)
            setupController.loadIdMarket(it)
            getProductList()
        }
    }

    private fun loadIdMarket(idMarket: Long) {
        _state.value = state.value.copy(
            marketId = idMarket
        )
        productController.loadIdMarket(idMarket)
        setupController.loadIdMarket(idMarket)
    }

    private fun initialAmountSetup(idMarket: Long) {
        val rate = setupController.rate.value.number
        setupController.onEvent(AmountSetupEvent.LoadSetup(idMarket))
        val newRate = setupController.rate.value.number
        if (newRate == rate) return
        productController.onEvent(ProductEvent.UpdateRate(idMarket))

    }

    private fun getProductList() {
        if (state.value.marketId == null) return
        getNotesJob?.cancel()
        getNotesJob = useCase.getAllProducts(state.value.marketId!!)
            .onEach { productList ->
                _state.value = state.value.copy(
                    list = productList
                )
            }
            .launchIn(viewModelScope)
    }



}