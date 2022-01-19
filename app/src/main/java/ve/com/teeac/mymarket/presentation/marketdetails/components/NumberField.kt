package ve.com.teeac.mymarket.presentation.marketdetails.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ve.com.teeac.mymarket.R


@Composable
fun NumberField(
    number: Number?,
    label: @Composable () -> Unit,
    onNumberChange: (number: Number?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions =  KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    testTag: String = "",
) {
    val numberInt = remember { mutableStateOf(number) }
    val textValue = remember(number != numberInt.value) {
        numberInt.value = number
        mutableStateOf(number?.toDouble()?.let {
            if (it % 1.0 == 0.0) {
                it.toInt().toString()
            } else {
                it.toString()
            }
        } ?: "")
    }

    val numberRegex = remember { "[-]?[\\d]*[.]?[\\d]*".toRegex() }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = textValue.value,
            onValueChange = {
                if (numberRegex.matches(it)) {
                    textValue.value = it
                    numberInt.value = it.toDoubleOrNull()
                    onNumberChange(numberInt.value)
                }
            },
            label = label,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(
                textAlign = TextAlign.End
            ),

            modifier = Modifier
                .testTag(testTag)
                .fillMaxWidth(),
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumberFieldTest() {

    var numbers by remember { mutableStateOf<Number?>(null) }

    NumberField(
        number = numbers,
        onNumberChange = { numbers = it },
        label = { Text(text = stringResource(R.string.word_testing)) }
    )

}

