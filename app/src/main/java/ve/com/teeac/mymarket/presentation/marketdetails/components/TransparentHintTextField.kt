package ve.com.teeac.mymarket.presentation.marketdetails.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle


@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    isHintVisible: Boolean = true,
    singleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
    testTag: String = ""
) {
    Box (
        modifier = modifier
    ){
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = textStyle,
            singleLine = singleLine,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
                .onFocusChanged {
                    onFocusChange(it)
                }
        )
        if(isHintVisible){
            Text(
                text = hint,
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray,
                fontSize = textStyle.fontSize,
                fontFamily = textStyle.fontFamily
            )
        }
    }

}

@SuppressLint("UnrememberedMutableState")
@Composable
fun HintPreview() {
    val stateIsVisible = mutableStateOf(true)
    val isV: State<Boolean> = stateIsVisible
    val stateCharter = mutableStateOf("")
    val stateT: State<String> = stateCharter
    TransparentHintTextField(
        text = stateT.value,
        hint = "Please write your name",
        isHintVisible = isV.value,
        onValueChange =  { stateCharter.value = it },
        onFocusChange= {stateIsVisible.value = !stateIsVisible.value && stateT.value.isBlank()},
        textStyle = MaterialTheme.typography.h5
    )
}

//@Preview(showBackground = true, heightDp = 35)
@Composable
fun TextPreview() {

    TransparentHintTextField(
        text = "My Nae is Thoth",
        hint = "Please write your name",
        isHintVisible = false,
        onValueChange =  {  },
        onFocusChange= {},
        textStyle = MaterialTheme.typography.h5
    )
}




