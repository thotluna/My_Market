package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.AmountsSetup

@Composable
fun Totals(amountDollar: Double, amount: Double, amountsSetup: AmountsSetup?) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.fillMaxWidth()
    ){
        Card(
            backgroundColor = MaterialTheme.colors.primary,
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth(.6f),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                val colorDollar: Color = amountsSetup?.maximumAvailableDollar?.let {
                    if(amountDollar > it){
                        MaterialTheme.colors.error
                    }else{
                        Color.White
                    }
                } ?: Color.White

                Text(text = "TOTALS")

                Text(
                    text = stringResource(
                        id = R.string.string_dollar,
                        amountDollar
                    ),
                    color = colorDollar
                )
                Text(
                    stringResource(
                        id = R.string.string_bs,
                        amount
                    ),
                    color = colorDollar
                )
            }
        }
    }
}