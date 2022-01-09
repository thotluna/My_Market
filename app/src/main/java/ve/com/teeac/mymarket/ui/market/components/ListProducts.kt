package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.MarketDetail

@Composable
fun ListProductsCard(value: List<MarketDetail>) {
    CardMarket(modifier = Modifier.fillMaxHeight(.9f)) {
        if (value.isNotEmpty()) {
            ListProducts(value)
        } else {
            ListEmptyMessage()
        }
    }

}

@Composable
private fun ListEmptyMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.3f),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Sin Mercados", style = MaterialTheme.typography.h3)
    }
}

@Composable
fun ListProducts(value: List<MarketDetail>) {
    LazyColumn {
        items(value) { item ->
            Product(detail = item)
        }
    }
}


@Composable
fun Product(detail: MarketDetail, modifier: Modifier = Modifier) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    id = R.string.string_quantity,
                    detail.quantity
                ),
                modifier = modifier.weight(1f)
            )
            Text(
                text = detail.description,
                modifier = modifier
                    .weight(2f)
                    .padding(end = 2.dp)
            )
            Text(
                text =
                stringResource(
                    id = R.string.string_dollar,
                    detail.unitAmountDollar
                ),
                modifier = modifier.weight(1f)
            )
//            Text(
//                text =
//                stringResource(
//                    id = R.string.string_bs,
//                    detail.unitAmount
//                ),
//                modifier = modifier.weight(1f)
//            )
            Text(
                text =
                stringResource(
                    id = R.string.string_dollar,
                    detail.amountDollar
                ),
                modifier = modifier.weight(1f)
            )
            Text(
                text =
                stringResource(
                    id = R.string.string_bs,
                    detail.amount
                ),
                modifier = modifier.weight(1f)
            )


        }
    }
}