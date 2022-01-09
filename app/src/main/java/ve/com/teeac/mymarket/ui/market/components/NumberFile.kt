package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberField(
    value: Number?,
    onNumberChange: (Number?) -> Unit,
    name: String,
    modifier: Modifier = Modifier
) {
    val number = remember { mutableStateOf(value) }
    val textValue = remember(value != number.value) {
        number.value = value
        mutableStateOf(value?.toDouble()?.let {
            if (it % 1.0 == 0.0) {
                it.toInt().toString()
            } else {
                it.toString()
            }
        } ?: "")
    }

    val numberRegex = remember { "[-]?[\\d]*[.]?[\\d]*".toRegex() }
    // for no negative numbers use "[\d]*[.]?[\d]*"

    OutlinedTextField(
        value = textValue.value,
        onValueChange = {
            if (numberRegex.matches(it)) {
                textValue.value = it
                number.value = it.toDoubleOrNull()
                onNumberChange(number.value)
            }
        },
        label = { Text(text = name) },
        placeholder = { Text(text = name) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        modifier = modifier
    )
}