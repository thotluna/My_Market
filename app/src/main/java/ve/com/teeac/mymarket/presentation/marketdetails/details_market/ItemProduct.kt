package ve.com.teeac.mymarket.presentation.marketdetails.details_market

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.MarketDetail

@Composable
fun ItemProduct(
    onClick: (id: Long) -> Unit,
    item: MarketDetail,
    onChecked: (id: Long, value: Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = item.isActive,
                onCheckedChange = { onChecked(item.id!!, item.isActive) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary
                )
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clickable { onClick(item.id!!) },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.quantity.toString(),
//                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.widthIn(8.dp))
                    Text(
                        text = item.description,
                        modifier = Modifier.weight(3f),
//                    style = MaterialTheme.typography.h5
                        fontSize = 16.sp,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Por Unidad",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = stringResource(
                            id = R.string.string_bs,
                            item.unitAmount
                        ),

                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = stringResource(
                            id = R.string.string_dollar,
                            item.unitAmountDollar
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
                Row {
                    Text(
                        text = "Total",
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = stringResource(
                            id = R.string.string_bs,
                            item.amount
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = stringResource(
                            id = R.string.string_dollar,
                            item.amountDollar
                        ),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemProductPreview() {
    val product = MarketDetail(
        id = null,
        marketId = 1L,
        quantity = 1.0,
        description = "Cosa",
        unitAmount = 50.0,
        amount = 50.0
    )
    ItemProduct(
        item = product,
        onClick = {},
        onChecked = { _, _ ->

        }
    )
}