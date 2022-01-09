package ve.com.teeac.mymarket.presentation.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ve.com.teeac.mymarket.R

@Composable
fun FloatingButton(onNew: (id: Int?) -> Unit) {
    FloatingActionButton(
        onClick = { onNew(null) },
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_market))
    }
}