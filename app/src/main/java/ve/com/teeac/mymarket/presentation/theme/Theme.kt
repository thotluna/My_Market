package ve.com.teeac.mymarket.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Orange_500,
    onPrimary = Color.White,
    secondary = Teal_500,
    onSecondary = Teal200,
    background = Blue_900,
    onBackground = Color.White,
    surface = Blue_700,
    onSurface = Color.White,
)

@Composable
fun MyMarketTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = DarkColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}