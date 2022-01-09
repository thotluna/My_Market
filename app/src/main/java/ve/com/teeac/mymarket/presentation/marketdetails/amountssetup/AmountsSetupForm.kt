package ve.com.teeac.mymarket.presentation.marketdetails.amountssetup

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.marketdetails.components.NumberField

@Composable
fun AmountSetupForm(
    converterState: AmountSetupState,
    amountState: AmountSetupState,
    amountsDollarState: AmountSetupState
) {
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
                .weight(1f),
            testTag = "Tag_Field_${ converterState.field.title }"
        )
        Spacer(modifier = Modifier.widthIn(2.dp))
        NumberField(
            number = amountState.field.number,
            onNumberChange = amountState.onChange,
            label = { Text(amountState.field.title) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f),
            testTag ="Tag_Field_${ amountState.field.title }"
        )
        Spacer(modifier = Modifier.widthIn(2.dp))
        NumberField(
            number = amountsDollarState.field.number,
            onNumberChange = amountsDollarState.onChange,
            label = { Text(amountsDollarState.field.title) },
            singleLine = true,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f),
            testTag ="Tag_Field_${ amountsDollarState.field.title }"
        )
    }
}

@Preview
@Composable
private fun AmountSetupFormPreview() {
    val converterState= AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "Taza"), onChange = {} )
    val amountState = AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "Bs"), onChange = {} )
    val amountsDollarState = AmountSetupState(field = NumberTextFieldState(number = 5.00, title = "$"), onChange = {} )

    MyMarketApp {
        AmountSetupForm(
            converterState = converterState,
            amountState = amountState,
            amountsDollarState = amountsDollarState
        )
    }
}