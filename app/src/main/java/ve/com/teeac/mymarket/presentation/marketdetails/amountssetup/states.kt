package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup


data class AmountSetupState (
    val  field: NumberTextFieldState,
    val onChange: (number: Number?) -> Unit
)

sealed class AmountSetupEvent{
    data class EnteredConvert(val value: Number?): AmountSetupEvent()
    data class EnteredAmounts(val value: Number?): AmountSetupEvent()
    data class EnteredAmountsDollar(val value: Number?): AmountSetupEvent()
}

data class NumberTextFieldState(
    val number: Number? = null,
    val title: String = "",
)
