package ve.com.teeac.mymarket.presentation.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import ve.com.teeac.mymarket.presentation.theme.MyMarketTheme

@Composable
fun MyMarketApp(content: @Composable () -> Unit ){
    MyMarketTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}