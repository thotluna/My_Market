package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.model.MarketDetail
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

    private val _totalBolivares = mutableStateOf(TotalStatus())
    val totalBolivares: State<TotalStatus> = _totalBolivares

    private val _totalDollar = mutableStateOf(TotalStatus())
    val totalDollar: State<TotalStatus> = _totalDollar

    private val _availableDollars = mutableStateOf(TotalStatus())
    val availableDollars: State<TotalStatus> = _availableDollars

    private val _availableBolivares = mutableStateOf(TotalStatus())
    val availableBolivares: State<TotalStatus> = _availableBolivares

    init {
        savedStateHandle.get<Long>("marketId")?.let {
            if (it > -1) {
                savedStateHandle.get<Long?>("marketId")?.let { idMarket ->
                    viewModelScope.launch {
                        loadIdMarket(idMarket)
                        initialAmountSetup(idMarket)
                        getProductList()
                    }


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
                        if (!productController.isSectionVisible.value) productController.onToggleSection()
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
            is DetailsMarketEvent.ChangedActivatedProduct -> {
                updateActivatedProduct(id = event.idProduct, isActive = !event.value)
            }
            is DetailsMarketEvent.DeleteProduct -> {
                viewModelScope.launch {
                    productController.deleteProduct(event.idProduct)
                    loadTotals()
                }
            }
            is DetailsMarketEvent.ClearProductForm -> {
                productController.clear()
            }
        }
    }

    private fun updateActivatedProduct(id: Long, isActive: Boolean) {
        viewModelScope.launch{
            useCase.updateActivatedProduct(id, isActive)
            getProductList()
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

    private fun updateMarket() {
        val market = Market(
            id = state.value.marketId,
            amountDollar = totalDollar.value.amount,
            amount = totalBolivares.value.amount
        )
        viewModelScope.launch {
            updateMarketDb(market)
        }
    }

    private suspend fun updateMarketDb(market: Market) {
        withContext(Dispatchers.IO) {
            useCase.addMarket(market)
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
        viewModelScope.launch {
            setupController.loadSetup(AmountSetupEvent.LoadSetup(idMarket))
            val newRate = setupController.rate.value.number
            if (newRate == rate) return@launch
            productController.onEvent(ProductEvent.UpdateRate(idMarket))

        }

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
            .onEach {
                loadTotals()
            }
            .launchIn(viewModelScope)
    }

    private fun loadTotals(list: List<MarketDetail>? = null) {

        val sumBolivares: Double
        val sumDollars: Double

        if(list != null){
            sumBolivares = calculateTotalDollar(list)
            sumDollars = calculateTotalBolivares(list)
        }else{
            sumBolivares =  calculateTotalBolivares(state.value.list)
            sumDollars =  calculateTotalDollar(state.value.list)
        }

        _totalBolivares.value = totalBolivares.value.copy(
            amount = sumBolivares,
            itExceeds = colorTotal(
                maximus = setupController.maxBolivares.value.number,
                amount = sumBolivares
            )
        )

        _totalDollar.value = totalDollar.value.copy(
            amount = sumDollars,
            itExceeds = colorTotal(
                maximus = setupController.maxDollar.value.number,
                amount = sumDollars
            )
        )

        _availableBolivares.value = availableBolivares.value.copy(
            amount = calculateAvailableBolivares()
        )
        _availableDollars.value = availableDollars.value.copy(
            amount = calculateAvailableDollars()
        )

        updateMarket()
    }

    private fun calculateTotalDollar(list: List<MarketDetail>): Double{
        return list.filter { it.isActive}.sumOf { it.amountDollar }
    }
    private fun calculateTotalBolivares(list: List<MarketDetail>): Double{
        return list.filter { it.isActive}.sumOf { it.amount }
    }

    private fun calculateAvailableDollars(): Double{
        return setupController.maxDollar.value.number?.let {
            it.toDouble() - totalDollar.value.amount
        }?: 0.00
    }
    private fun calculateAvailableBolivares(): Double{
        return setupController.maxBolivares.value.number?.let {
            it.toDouble() - totalBolivares.value.amount
        }?: 0.00
    }

    private fun colorTotal(maximus: Number?, amount: Double): Boolean {
        return maximus?.let {
            return it.toDouble() < amount
        } ?: false

    }
}


data class TotalStatus(
    val amount: Double = 0.0,
    val itExceeds: Boolean = false
)