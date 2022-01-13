package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.domain.usecases.DetailsMarketUseCase
import ve.com.teeac.mymarket.presentation.InvalidEventException
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupEvent
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.SetupController
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductEvent
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductFormController
import javax.inject.Inject

@HiltViewModel
class DetailsMarketViewModel @Inject constructor(
    private val useCase: DetailsMarketUseCase,
    var setupController: SetupController,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var getNotesJob: Job? = null

    val productController = ProductFormController()

    private val _state = mutableStateOf(DetailsMarketState(
        marketId = -1L
    ))
    val state: State<DetailsMarketState> = _state

    private val _idSetupSectionVisible = mutableStateOf(false)
    val idSetupSectionVisible: State<Boolean> = _idSetupSectionVisible

    private val _idProductSectionVisible = mutableStateOf(false)
    val idProductSectionVisible: State<Boolean> = _idProductSectionVisible



    init {

        if (savedStateHandle.get<Long>("marketId")!! > -1) {
            savedStateHandle.get<Long?>("marketId")?.let { idMarket ->
                loadIdMarket(idMarket)
                viewModelScope.launch {
                    initialAmountSetup(idMarket)
                }
                getProductList()

            }
        }

        viewModelScope.launch{
            setupController.rateFlow.collectLatest { rate ->
                rate.number?.let{
                    if(it == productController.rate.value && it.toDouble() <= 0) return@let
                    productController.onEvent(ProductEvent.UpdateRate(it))
                }
            }
        }
    }

    fun onToggleProductSection() {
        _idProductSectionVisible.value = !idProductSectionVisible.value
    }

    fun onToggleSetupSection() {
        _idSetupSectionVisible.value = !idSetupSectionVisible.value
    }

    fun onEvent(event: DetailsMarketEvent) {
        when (event) {

            is DetailsMarketEvent.SaveAmountSetup -> {
                state.value.marketId?.let{
                    setupController.onEvent(AmountSetupEvent.Save(it))
                }

            }
            is DetailsMarketEvent.UpdateProduct -> {
                viewModelScope.launch {

                    useCase.getProduct(event.idProduct).also {
                        it?.let { detail ->
                            productController.onEvent(ProductEvent.UpdateProduct(detail))
                            _idProductSectionVisible.value = true
                        }
                    }
                }
            }
            is DetailsMarketEvent.SaveProduct -> {

                viewModelScope.launch {
                    val id = getMarketId()
                    useCase.addProduct(productController.getMarketDetail(id))
                    productController.clear()
                }
            }
            is DetailsMarketEvent.DeleteProduct -> {
                viewModelScope.launch{
                    useCase.deleteProduct(event.idProduct)
                }
            }
            is DetailsMarketEvent.ClearProductForm -> {
                productController.clear()
            }
            else -> throw InvalidEventException(InvalidEventException.DETAILS_MARKET_EVENT)
        }
    }

    private suspend fun getMarketId(): Long {
        return if (state.value.marketId != null) {
            state.value.marketId!!
        } else {
            createMarket()
        }
    }

    private suspend fun createMarket(): Long {
        return useCase.addMarket(Market()).also {
            _state.value = state.value.copy(
                marketId = it
            )
        }
    }

    private fun loadIdMarket(idMarket: Long) {
        _state.value = state.value.copy(
            marketId = idMarket
        )
    }

    private fun initialAmountSetup(idMarket: Long) {
        val rate = setupController.rate.value.number
        setupController.onEvent(AmountSetupEvent.LoadSetup(idMarket))
        val newRate = setupController.rate.value.number
        if(newRate == rate) return
        productController.onEvent(ProductEvent.UpdateRate(idMarket))

    }

    private fun getProductList() {
        if(state.value.marketId == null) return
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