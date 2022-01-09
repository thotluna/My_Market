package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import ve.com.teeac.mymarket.R


@Composable
fun ScaffoldApp(topBar: @Composable () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = topBar
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            content()
        }
    }
}

@Composable
fun TopBarMarket() {
    TopAppBar(
        title = {
            ActionBarButton(imageVector = Icons.Default.ArrowBack, {/*TODO*/ })
            Text(text = stringResource(id = R.string.title_market_screen))
        },
        actions = {
            ActionBarButton(imageVector = Icons.Default.Share, onClick = { /*TODO*/ })
            ActionBarButton(imageVector = Icons.Default.Settings, onClick = { /*TODO*/ })

        }
    )
}

@Composable
private fun ActionBarButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null
) {
    IconButton(onClick) {
        Icon(
            imageVector,
            contentDescription
        )
    }
}