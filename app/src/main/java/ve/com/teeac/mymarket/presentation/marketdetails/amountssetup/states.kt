package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.ui.focus.FocusState
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState


data class AmountSetupState (
    val  field: NumberTextFieldState,
    val onChange: (number: Number?) -> Unit
)

sealed class AmountSetupEvent{
    data class EnteredConvert(val value: Number?): AmountSetupEvent()
    data class ChangeConvert(val focusState: FocusState): AmountSetupEvent()
    data class EnteredAmounts(val value: Number?): AmountSetupEvent()
    data class ChangeAmountBs(val focusState: FocusState): AmountSetupEvent()
    data class EnteredAmountsDollar(val value: Number?): AmountSetupEvent()
    data class ChangeAmountDollar(val focusState: FocusState): AmountSetupEvent()
}


