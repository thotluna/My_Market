package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.domain.usecases.SetupUseCase
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState

class SetupController(
    private var valueInitial: AmountsSetup = AmountsSetup(),
    private val useCase: SetupUseCase
) {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

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
            number = if(valueInitial.maximumAvailable > 0L) valueInitial.maximumAvailable else null
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

    val rateFlow = flow {
        emit(_rate.value)
    }

    fun onEvent(event: AmountSetupEvent) {
        when (event) {
            is AmountSetupEvent.EnteredRate -> changeRate(event.value)
            is AmountSetupEvent.EnteredMaxBolivares -> changeMaxBolivares(event.value)
            is AmountSetupEvent.EnteredMaxDollar -> changeMaxDollar(event.value)
            is AmountSetupEvent.Save -> save(event)
            is AmountSetupEvent.LoadSetup -> {
                scope.launch{
                    loadSetup(event)
                }
            }

        }
    }

    @VisibleForTesting
    suspend fun loadSetup(event: AmountSetupEvent.LoadSetup) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)

            val newSetup: AmountsSetup? = getSetupDb(event.idMarket)

            newSetup?.let {
                valueInitial.id = it.id
                valueInitial.marketId = it.marketId
                changeRate(it.rate)
                changeMaxBolivares(it.maximumAvailable)
                changeMaxDollar(it.maximumAvailableDollar)
            }

    }

    private suspend fun getSetupDb(idMarket: Long): AmountsSetup?{
        return withContext(context = Dispatchers.IO) {
            return@withContext useCase.getSetup(idMarket)
        }
    }

    private fun changeMaxDollar(number: Number?) {
        if (number == maxDollar.value.number) return
        _maxDollar.value = maxDollar.value.copy(number = number)
    }

    private fun changeMaxBolivares(number: Number?) {
        if (number == maxBolivares.value.number) return
        _maxBolivares.value = maxBolivares.value.copy(number = number)
    }

    private fun changeRate(number: Number?) {
        if (number == rate.value.number || number!!.toDouble() <= 0) return
        _rate.value = rate.value.copy(number = number)
    }

    @VisibleForTesting
    fun save(event: AmountSetupEvent.Save){
        scope.launch {
            useCase.addSetup(getAmountSetup(event.idMarket)).also {
                valueInitial = valueInitial.copy(id = it )
            }
        }
    }

    @VisibleForTesting
    fun getAmountSetup(idMarket: Long): AmountsSetup {
        return AmountsSetup(
            id = valueInitial.id,
            rate = rate.value.number?.toDouble() ?: 0.0,
            maximumAvailable = maxBolivares.value.number?.toDouble() ?: 0.0,
            maximumAvailableDollar = maxDollar.value.number?.toDouble() ?: 0.0,
            marketId = idMarket
        )
    }

}
