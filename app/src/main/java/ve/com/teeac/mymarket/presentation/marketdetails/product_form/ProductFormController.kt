package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.domain.usecases.product_use_cases.ProductUseCase
import ve.com.teeac.mymarket.presentation.InvalidPropertyApp
import ve.com.teeac.mymarket.presentation.marketdetails.NoteTextFieldState
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState
import kotlin.math.round
import kotlin.math.roundToInt

class ProductFormController(
    private val useCase: ProductUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO
) {

    private val scope = CoroutineScope(Job() + dispatcher)

    private val _idMarket = mutableStateOf<Long?>(null)
    val idMarket: State<Long?> = _idMarket

    private val _rate = mutableStateOf<Number>(0)
    val rate: State<Number?> = _rate

    private val _id = mutableStateOf<Long?>(null)
    val id: State<Long?> = _id

    private val _quantity = mutableStateOf(NumberTextFieldState(title = "Cantidad"))
    val quantity: State<NumberTextFieldState> = _quantity

    private val _description = mutableStateOf(NoteTextFieldState(hint = "Descripción"))
    val description: State<NoteTextFieldState> = _description

    private val _amountDollar = mutableStateOf(NumberTextFieldState(title = "$"))
    val amountDollar: State<NumberTextFieldState> = _amountDollar

    private val _amountBs = mutableStateOf(NumberTextFieldState(title = "Bs"))
    val amountBs: State<NumberTextFieldState> = _amountBs

    private val _idSectionVisible = mutableStateOf(false)
    val isSectionVisible: State<Boolean> = _idSectionVisible

    fun loadIdMarket(id: Long) {
        _idMarket.value = id
    }

    fun onToggleSection() {
        _idSectionVisible.value = !isSectionVisible.value
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.EnteredQuantity -> enteredQuality(event.value)
            is ProductEvent.EnteredDescription -> enteredDescription(event.value)
            is ProductEvent.EnteredAmountBs -> enteredAmountBs(event.value)
            is ProductEvent.EnteredAmountDollar -> enteredAmountDollar(event.value)
            is ProductEvent.LoadProduct -> {
                scope.launch {
                    loadProduct(event.id)
                }
            }
            is ProductEvent.Save -> {
                scope.launch {
                    saveProduct(event.idMarket)
                }
            }
            is ProductEvent.UpdateRate -> updateRate(event)
        }
    }

    private fun set(product: MarketDetail) {
        _idMarket.value = product.marketId
        _id.value = product.id
        _quantity.value = quantity.value.copy(number = product.quantity)
        _description.value = description.value.copy(text = product.description)
        _amountDollar.value = amountDollar.value.copy(number = product.unitAmountDollar)
        _amountBs.value = amountBs.value.copy(number = product.unitAmount)
    }

    private fun get(idMarket: Long): MarketDetail = MarketDetail(
        id = id.value,
        marketId = idMarket,
        quantity = quantity.value.number?.toDouble() ?: 1.0,
        description = description.value.text ?: "",
        unitAmount = amountBs.value.number?.toDouble() ?: 0.0,
        unitAmountDollar = amountDollar.value.number?.toDouble() ?: 0.00,
        amount = getAmountBsTotal(),
        amountDollar = getAmountDollarTotal(),
    )

    suspend fun saveProduct(idMarket: Long) {
        withContext(dispatcherIo){
            useCase.addProduct(get(idMarket))
        }
        clear()
    }

    private fun updateRate(event: ProductEvent.UpdateRate) {
        _rate.value = event.rate?: 0.0

        idMarket.value?.let {
            scope.launch {
                useCase.updateProducts(rate.value!!.toDouble(), marketId = it)
            }
        }

    }

    suspend fun deleteProduct(id: Long){
        withContext(dispatcherIo){
            useCase.deleteProduct(id)
        }
    }

    suspend fun loadProduct(id: Long) {
        getProduct(id)?.let {
            set(it)
        }?: throw InvalidPropertyApp(InvalidPropertyApp.PRODUCT_DO_NOT_EXIST)
    }

    private suspend fun getProduct(id: Long): MarketDetail? {
        return withContext(dispatcherIo) {
            return@withContext useCase.getProduct(id)
        }
    }

    private fun enteredAmountDollar(number: Number?) {
        if (number == amountDollar.value.number) return
        _amountDollar.value = amountDollar.value.copy(number = number)
        _amountBs.value = amountBs.value.copy(
            number = amountDollar.value.number?.let{
                it.toDouble() * _rate.value.toDouble()
            }?: 0.00
        )
    }

    private fun enteredAmountBs(number: Number?) {
        if (number == amountBs.value.number) return
        _amountBs.value = amountBs.value.copy(number = number)
        if (_rate.value.toDouble() > 0) {
            _amountDollar.value = amountDollar.value.copy(
                number = amountBs.value.number?.let{
                    ((it.toDouble() / _rate.value.toDouble()) * 1000.0).roundToInt() / 1000.0

                }?:0.00
            )
        }
    }

    private fun enteredDescription(text: String?) {
        if (text == description.value.text) return
        _description.value = description.value.copy(text = text)
    }

    private fun enteredQuality(number: Number?) {
        if (number == quantity.value.number) return
        _quantity.value = quantity.value.copy(number = number)
    }

    private fun getAmountBsTotal(): Double {
        return quantity.value.number?.let { quantity ->
            if (amountBs.value.number != null) {
                amountBs.value.number!!.toDouble() * quantity.toDouble()
            } else {
                0.00
            }
        } ?: 0.0
    }

    private fun getAmountDollarTotal(): Double {
        return quantity.value.number?.let { quantity ->
            if (amountDollar.value.number != null) {
                _amountDollar.value.number!!.toDouble() * quantity.toDouble()
            } else {
                0.00
            }
        } ?: 0.0
    }

    fun clear() {
        _id.value = null
        _quantity.value = quantity.value.copy(number = null)
        _description.value = description.value.copy(text = null)
        _amountBs.value = amountBs.value.copy(number = null)
        _amountDollar.value = amountDollar.value.copy(number = null)
    }


}
