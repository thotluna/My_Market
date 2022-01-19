package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState


data class AmountSetupState (
    val  field: NumberTextFieldState,
    val onChange: (number: Number?) -> Unit
)

sealed class AmountSetupEvent{
    data class EnteredRate(val value: Number?): AmountSetupEvent()
    data class EnteredMaxBolivares(val value: Number?): AmountSetupEvent()
    data class EnteredMaxDollar(val value: Number?): AmountSetupEvent()

    data class Save(val idMarket: Long): AmountSetupEvent()
    data class LoadSetup(val idMarket: Long): AmountSetupEvent()
}


