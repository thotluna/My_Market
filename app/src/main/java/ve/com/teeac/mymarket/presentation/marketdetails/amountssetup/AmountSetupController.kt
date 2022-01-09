package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ve.com.teeac.mymarket.domain.model.AmountsSetup

class AmountSetupController {

    companion object {
        const val NAME_RATE = "Taza"
        const val NAME_BOLIVARES = "Bs"
        const val NAME_DOLLAR = "$"
    }

    private val _rate = mutableStateOf(
        NumberTextFieldState(
        title = NAME_RATE
    )
    )
    val rate: State<NumberTextFieldState> = _rate

    private val _maxBolivares = mutableStateOf(
        NumberTextFieldState(
        title = NAME_BOLIVARES
    )
    )
    val maxBolivares: State<NumberTextFieldState> = _maxBolivares

    private val _maxDollar = mutableStateOf(
        NumberTextFieldState(
        title = NAME_DOLLAR
    )
    )
    val maxDollar: State<NumberTextFieldState> = _maxDollar

    fun onAmountsSetupEvent(event: AmountSetupEvent) {
        when (event) {
            is AmountSetupEvent.EnteredConvert -> {
                if (event.value == rate.value.number) return
                _rate.value = rate.value.copy(number = event.value)
            }
            is AmountSetupEvent.EnteredAmounts -> {
                if (event.value == maxBolivares.value.number) return
                _maxBolivares.value = maxBolivares.value.copy(number = event.value)
            }
            is AmountSetupEvent.EnteredAmountsDollar -> {
                if (event.value == maxDollar.value.number) return
                _maxDollar.value = maxDollar.value.copy(number = event.value)
            }

        }
    }

    fun getAmountSetup(idMarket: Long): AmountsSetup{
        return AmountsSetup(
            rate = rate.value.number?.toDouble()?: 0.0,
            maximumAvailable = maxBolivares.value.number?.toDouble()?: 0.0,
            maximumAvailableDollar = maxDollar.value.number?.toDouble()?: 0.0,
            marketId = idMarket
        )
    }

}