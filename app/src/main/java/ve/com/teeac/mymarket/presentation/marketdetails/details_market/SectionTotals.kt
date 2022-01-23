package ve.com.teeac.mymarket.presentation.marketdetails.details_market

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.presentation.marketdetails.TotalStatus

@Composable
fun SectionTotals(
    currentDollars: TotalStatus,
    currentBolivares: TotalStatus,
    availableBolivares: TotalStatus,
    availableDollars: TotalStatus
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { contentDescription = "Totals" },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Totals"
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            Text(
                text = stringResource(
                    id = R.string.string_dollar,
                    currentDollars.amount
                ),
                color = if (currentDollars.itExceeds) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            Text(
                text = stringResource(
                    id = R.string.string_bs,
                    currentBolivares.amount
                ),
                color = if (currentBolivares.itExceeds) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Quedan"
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            Text(
                text = stringResource(
                    id = R.string.string_dollar,
                    availableDollars.amount
                ),
                color = if (currentDollars.itExceeds) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            )
            Spacer(modifier = Modifier.widthIn(8.dp))
            Text(
                text = stringResource(
                    id = R.string.string_bs,
                    availableBolivares.amount
                ),
                color = if (currentBolivares.itExceeds) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onPrimary
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SectionTotalsPreview() {
    val dollars = remember { mutableStateOf(TotalStatus(amount = 25.00, itExceeds = true)) }
    val bolivares = remember { mutableStateOf(TotalStatus(amount = 125.00, itExceeds = true)) }
    val availableBolivares =
        remember { mutableStateOf(TotalStatus(amount = 125.00, itExceeds = true)) }
    val availableDollars =
        remember { mutableStateOf(TotalStatus(amount = 125.00, itExceeds = true)) }

    SectionTotals(
        currentBolivares = bolivares.value,
        currentDollars = dollars.value,
        availableDollars = availableDollars.value,
        availableBolivares = availableBolivares.value

    )
}