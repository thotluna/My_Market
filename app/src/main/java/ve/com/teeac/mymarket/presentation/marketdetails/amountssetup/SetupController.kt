package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.usecases.SetupUseCase
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState

class SetupController(
    private var valueInitial: AmountsSetup = AmountsSetup(),
    private val useCase: SetupUseCase,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO
) {

    private val scope = CoroutineScope(Job() + dispatcher)

    companion object {
        const val NAME_RATE = "Taza"
        const val NAME_BOLIVARES = "Bs"
        const val NAME_DOLLAR = "$"
    }

    private val _rate = mutableStateOf(
        NumberTextFieldState(
            title = NAME_RATE,
            number = if(valueInitial.rate > 0L) valueInitial.rate else null
        )
    )
    val rate: State<NumberTextFieldState> = _rate

    private val _maxBolivares = mutableStateOf(
        NumberTextFieldState(
            title = NAME_BOLIVARES,
            number = if(valueInitial.maximumAvailableBolivares > 0L) valueInitial.maximumAvailableBolivares else null
        )
    )
    val maxBolivares: State<NumberTextFieldState> = _maxBolivares

    private val _maxDollar = mutableStateOf(
        NumberTextFieldState(
            title = NAME_DOLLAR,
            number = if(valueInitial.maximumAvailableDollar > 0L) valueInitial.maximumAvailableDollar else null
        )
    )
    val maxDollar: State<NumberTextFieldState> = _maxDollar

    private var isLoad = false

    private val _idSectionVisible = mutableStateOf(false)
    val isSectionVisible: State<Boolean> = _idSectionVisible

    fun loadIdMarket(idMarket: Long) {
        valueInitial.marketId = idMarket
    }

    fun onToggleSection() {
        _idSectionVisible.value = !isSectionVisible.value
    }

    fun onEvent(event: AmountSetupEvent) {
        when (event) {
            is AmountSetupEvent.EnteredRate -> changeRate(event.value)
            is AmountSetupEvent.EnteredMaxBolivares -> changeMaxBolivares(event.value)
            is AmountSetupEvent.EnteredMaxDollar -> changeMaxDollar(event.value)
            is AmountSetupEvent.Save -> {
                scope.launch{
                    save(event)
                }
            }
            is AmountSetupEvent.LoadSetup -> {
                scope.launch{
                    loadSetup(event)
                }
            }
        }
    }

    @VisibleForTesting
    fun get(idMarket: Long): AmountsSetup {
        return AmountsSetup(
            id = valueInitial.id,
            rate = rate.value.number?.toDouble() ?: 0.0,
            maximumAvailableBolivares = maxBolivares.value.number?.toDouble() ?: 0.0,
            maximumAvailableDollar = maxDollar.value.number?.toDouble() ?: 0.0,
            marketId = idMarket
        )
    }

    private fun set(it: AmountsSetup) {
        isLoad = true
        valueInitial.id = it.id
        valueInitial.marketId = it.marketId
        changeRate(it.rate)
        changeMaxBolivares(it.maximumAvailableBolivares)
        changeMaxDollar(it.maximumAvailableDollar)

        isLoad = false
    }

    suspend fun loadSetup(event: AmountSetupEvent.LoadSetup) {
           val newSetup: AmountsSetup? = getFromDatabase(event.idMarket)
            newSetup?.let {  set(it) }
    }

    private suspend fun getFromDatabase(idMarket: Long): AmountsSetup?{
        return withContext(context = Dispatchers.IO) {
            return@withContext useCase.getSetup(idMarket)
        }
    }

    private fun changeMaxDollar(number: Number?) {
        if (number == maxDollar.value.number) return
        _maxDollar.value = maxDollar.value.copy(number = number)

        if(isLoad) return
        rate.value.number?.let {
            if(rate.value.number == 0) return@let
            _maxBolivares.value = maxBolivares.value.copy(
                number = maxDollar.value.number?.let{ dollar ->
                    dollar.toDouble() * it.toDouble()
                }?: 0.00
            )
        }
    }

    private fun changeMaxBolivares(number: Number?) {
        if (number == maxBolivares.value.number) return
        _maxBolivares.value = maxBolivares.value.copy(number = number)

        if(isLoad) return
        rate.value.number?.let {
            if (rate.value.number == 0) return@let
            _maxDollar.value = maxDollar.value.copy(
                number = maxBolivares.value.number?.let{ bolivares ->
                    bolivares.toDouble() / it.toDouble()
                }?: 0.00
            )
        }
    }

    private fun changeRate(number: Number?) {
        number?.let{ numbers ->
            if (numbers == rate.value.number || numbers.toDouble() <= 0) return
        }
        _rate.value = rate.value.copy(number = number)
    }

    suspend fun save(event: AmountSetupEvent.Save){
        withContext(dispatcherIo) {
            useCase.addSetup(get(event.idMarket)).also {
                valueInitial = valueInitial.copy(id = it)

            }
        }
    }


    



}
