package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ve.com.teeac.mymarket.presentation.components.MyMarketApp
import ve.com.teeac.mymarket.presentation.marketdetails.NoteTextFieldState
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState
import ve.com.teeac.mymarket.presentation.marketdetails.components.NumberField
import ve.com.teeac.mymarket.utils.TestTags

@ExperimentalComposeUiApi
@Composable
fun ProductForm(
    quantity: NumberTextFieldState,
    quantityChange: (number: Number?) -> Unit,
    description: NoteTextFieldState,
    descriptionChange: (text: String?) -> Unit,
    amountBs: NumberTextFieldState,
    amountBsChange: (number: Number?) -> Unit,
    amountDollar: NumberTextFieldState,
    amountDollarChange: (number: Number?) -> Unit,
    onSave: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val (first, second, third, fourth) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { contentDescription = "ProductForm" },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,){
                NumberField(
                    number = quantity.number,
                    label = { Text(quantity.title, fontSize = 12.sp) },
                    onNumberChange = quantityChange,
                    modifier = Modifier
                        .focusOrder(first){down = second}
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    testTag = TestTags.QUALITY_FIELD
                )

                TextField(
                    value = description.text ?: "",
                    label = { Text(description.hint) },
                    onValueChange = descriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .focusOrder(second) { down = third }
                        .testTag(TestTags.DESCRIPTION_FIELD),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),

                    )
            }

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween){

                NumberField(
                    number = amountBs.number,
                    label = { Text(amountBs.title) },
                    onNumberChange = amountBsChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusOrder(third) { down = fourth },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    testTag = TestTags.AMOUNT_BS_FIELD
                )
                NumberField(
                    number = amountDollar.number,
                    label = { Text(amountDollar.title) },
                    onNumberChange = amountDollarChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusOrder(fourth) { down = first },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                            keyboardController?.hide()
                            onSave()
                        }),
                    testTag = TestTags.AMOUNT_DOLLAR_FIELD
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun ProductFormPreview() {
    val quantity = remember { mutableStateOf(NumberTextFieldState(title = "Cantidad")) }
    val description = remember { mutableStateOf(NoteTextFieldState(hint = "Descripcion") )}
    val amountDollar = remember { mutableStateOf(NumberTextFieldState(title = "$")) }
    val amountBs = remember { mutableStateOf(NumberTextFieldState(title = "Bs")) }


    MyMarketApp {
        ProductForm(
            quantity = quantity.value,
            quantityChange = {quantity.value = quantity.value.copy(number = it)},
            description = description.value,
            descriptionChange = {description.value = description.value.copy(text = it)},
            amountBs = amountBs.value,
            amountBsChange = {amountBs.value = amountBs.value.copy(number = it)},
            amountDollar = amountDollar.value,
            amountDollarChange = {amountDollar.value = amountDollar.value.copy(number = it)},
            onSave = {}
        )
    }
}
