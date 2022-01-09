package ve.com.teeac.mymarket.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
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

private val LightColorPalette = lightColors(
    primary = Blue_700,
    primaryVariant = Blue_900,
    secondary = Teal_500,
    background = Blue_900

    /*primary = Orange_500,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    background = Blue_900,
    onBackground = Blue_700,
    surface = Blue_500,
    onSurface = Purple200,*/

)

@Composable
fun MyMarketTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = DarkColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}