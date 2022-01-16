package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.domain.model.AmountsSetup
import ve.com.teeac.mymarket.R

@Composable
fun Amounts(value: AmountsSetup) {
    CardMarket {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /* TODO: cambiar datos de text por State viewModel */
            Text(text = "$/Bs.")
            Text(text = stringResource(
                id = R.string.string_bs,
                value.rate
            )
            )
            if(value.maximumAvailableBolivares != 0.0 ||
                value.maximumAvailableDollar != 0.0){
                Text(text = "Max. Disponible")
                Text(text = stringResource(
                    id = R.string.string_dollar,
                    value.maximumAvailableDollar
                )
                )
                Text(text = stringResource(
                    id = R.string.string_bs,
                    value.maximumAvailableBolivares
                )
                )
            }
        }

    }

}