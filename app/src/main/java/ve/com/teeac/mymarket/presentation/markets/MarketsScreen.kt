package ve.com.teeac.mymarket.presentation.markets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.flow.collectLatest
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.Market
import ve.com.teeac.mymarket.utils.TestTags
import ve.com.teeac.mymarket.utils.getDate
import java.text.NumberFormat
import java.util.*

@Composable
fun MarketsScreen(
    viewModel: MarketsViewModel = hiltViewModel(),
    showDetails: (marketId: Long) -> Unit,
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.NavigateToDetails -> {
                    showDetails(event.marketId)
                }
            }
        }
    }


    Scaffold(
//        modifier = Modifier.statusBarsPadding(),
        topBar = { TopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDetails(-1) }, //viewModel.onEvent(MarketsEvent.NavigateMarket())
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_market)
                )
            }
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .semantics { contentDescription = "Market Screen" }
        ) {
            if (state.markets.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(TestTags.LIST_MARKETS)
                ) {
                    items(state.markets) { market ->
                        ItemMarkets(
                            item = market,
                            onClick = {
                                showDetails(it)
                            }
                        )
                    }
                }
            } else {
                Text(text = stringResource(R.string.markets_empty))
            }
        }
    }
}

@Composable
fun ItemMarkets(item: Market, modifier: Modifier = Modifier, onClick: (id: Long) -> Unit) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(item.id!!) }
            .testTag(item.id.toString())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = getDate(item.date),
                style = MaterialTheme.typography.caption
            )
            Text(
                text = stringResource(
                    id = R.string.string_dollar,
                    item.amountDollar
                ),
                style = MaterialTheme.typography.caption
            )

            Text(
                text = stringResource(
                    id = R.string.string_bs,
                    item.amount
                ),
                style = MaterialTheme.typography.caption
            )

        }
    }
}


//@Preview(showBackground = true, widthDp = 350)
@Composable
private fun TopBar() {
    TopAppBar {
        Text(
            text = stringResource(R.string.title_app),
            modifier = Modifier.padding(vertical = 8.dp)
                .statusBarsPadding(),
        )
    }
}