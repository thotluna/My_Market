package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState
import ve.com.teeac.mymarket.presentation.marketdetails.components.NumberField

@ExperimentalComposeUiApi
@Composable
fun AmountSetupForm(
    converterState: AmountSetupState,
    amountState: AmountSetupState,
    amountsDollarState: AmountSetupState,
    onToggleSetupSection: () -> Unit,
    onSave: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val (secondSetup, thirdSetup, fourthSetup) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        NumberField(
            number = converterState.field.number,
            onNumberChange = converterState.onChange,
            label = { Text(converterState.field.title) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
                .focusOrder(secondSetup) { down = thirdSetup },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            testTag = "Tag_Field_${converterState.field.title}"
        )
        Spacer(modifier = Modifier.widthIn(2.dp))
        NumberField(
            number = amountState.field.number,
            onNumberChange = amountState.onChange,
            label = { Text(amountState.field.title) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
                .focusOrder(thirdSetup) { down = fourthSetup },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            testTag = "Tag_Field_${amountState.field.title}"
        )
        Spacer(modifier = Modifier.widthIn(2.dp))
        NumberField(
            number = amountsDollarState.field.number,
            onNumberChange = amountsDollarState.onChange,
            label = { Text(amountsDollarState.field.title) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
                .focusOrder(fourthSetup),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            testTag = "Tag_Field_${amountsDollarState.field.title}"
        )
        IconButton(
            onClick = {
                onSave()
                onToggleSetupSection()
            },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Guardar setup"
            )
        }
    }


}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun AmountSetupFormPreview() {
    val converterState =
        AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "Taza"), onChange = {})
    val amountState =
        AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "Bs"), onChange = {})
    val amountsDollarState =
        AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "$"), onChange = {})

    val idSetupSectionVisible = remember { mutableStateOf(false) }

    MyMarketApp {
        AmountSetupForm(
            converterState = converterState,
            amountState = amountState,
            amountsDollarState = amountsDollarState,
            onToggleSetupSection = { idSetupSectionVisible.value = !idSetupSectionVisible.value },
            onSave = { }
        )
    }
}