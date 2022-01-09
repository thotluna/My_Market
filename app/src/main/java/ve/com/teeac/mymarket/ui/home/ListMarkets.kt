package ve.com.teeac.mymarket.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.utils.getDate

@Composable
fun ListMarkets(listMarkets: State<List<Market>>, modifier: Modifier = Modifier) {

    Card(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if(listMarkets.value.isNotEmpty()){
            LazyColumn{
                items(listMarkets.value){ item ->
                    ItemMarkets(item = item)
                }
            }
        }else{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.3f),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Sin Mercados", style = MaterialTheme.typography.h3)
            }
        }
    }

}

@Composable
fun ItemMarkets(item: Market, modifier: Modifier = Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = getDate(item.date)
            )
            Text(text = stringResource(
                id = R.string.string_dollar,
                item.amountDollar
            )
            )
            Text(text = stringResource(
                id = R.string.string_bs,
                item.amount
            )
            )

        }
    }
}