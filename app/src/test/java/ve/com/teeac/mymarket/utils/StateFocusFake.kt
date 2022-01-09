package ve.com.teeac.mymarket.utils

import androidx.compose.ui.focus.FocusState

sealed class StateFocusFake{
    object InFocus: FocusState {
        override var hasFocus: Boolean = true
        override var isCaptured: Boolean = false
        override var isFocused: Boolean = true
    }

    object outFocus: FocusState {
        override var hasFocus: Boolean = false
        override var isCaptured: Boolean = false
        override var isFocused: Boolean = false
    }
}
