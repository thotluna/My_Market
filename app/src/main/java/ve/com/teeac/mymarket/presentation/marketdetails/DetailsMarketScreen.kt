package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupEvent
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupForm
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupState
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductEvent
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.ProductForm
import ve.com.teeac.mymarket.presentation.marketdetails.product_form.UiEvent
import ve.com.teeac.mymarket.utils.TestTags

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun DetailsMarketScreen(
    navigateUp: () -> Unit,
    viewModel: DetailsMarketViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    val converterState = viewModel.setupController.rate.value
    val amountState = viewModel.setupController.maxBolivares.value
    val amountsDollarState = viewModel.setupController.maxDollar.value

    val quantity = viewModel.productController.quantity.value
    val description = viewModel.productController.description.value
    val amountDollar = viewModel.productController.amountDollar.value
    val amountBs = viewModel.productController.amountBs.value

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar {
                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navigateUp() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Mercado ${viewModel.state.value.marketId}")
                }

                Row(
                    Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            viewModel.setupController.onToggleSection()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Open Setup Section"
                        )
                    }
                    Spacer(modifier = Modifier.widthIn(4.dp))
                    IconButton(
                        onClick = {
                            viewModel.productController.onToggleSection()
                            viewModel.onEvent(DetailsMarketEvent.ClearProductForm)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Open Product Section"
                        )
                    }
                }


            }

        },
        scaffoldState = scaffoldState
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
                .fillMaxSize()
                .semantics { contentDescription = "Detail Market Screen" }
        ) {
            AnimatedVisibility(
                visible = viewModel.setupController.isSectionVisible.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),

                ) {
                AmountSetupForm(
                    converterState = AmountSetupState(
                        converterState,
                        onChange = { number ->
                            viewModel.setupController
                                .onEvent(AmountSetupEvent.EnteredRate(number))
                        }
                    ),
                    amountState = AmountSetupState(
                        field = amountState,
                        onChange = { number ->
                            viewModel.setupController
                                .onEvent(AmountSetupEvent.EnteredMaxBolivares(number))
                        }
                    ),
                    amountsDollarState = AmountSetupState(
                        field = amountsDollarState,
                        onChange = { number ->
                            viewModel.setupController
                                .onEvent(AmountSetupEvent.EnteredMaxDollar(number))
                        }
                    ),
                    onSave = { viewModel.onEvent(DetailsMarketEvent.SaveAmountSetup) }
                )
                Spacer(modifier = Modifier.widthIn(8.dp))
            }
            AnimatedVisibility(
                visible = viewModel.productController.isSectionVisible.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                ProductForm(
                    quantity = quantity,
                    quantityChange = { number ->
                        viewModel.productController.onEvent(ProductEvent.EnteredQuantity(number))
                    },
                    description = description,
                    descriptionChange = { word ->
                        viewModel.productController.onEvent(ProductEvent.EnteredDescription(word))
                    },
                    amountBs = amountBs,
                    amountBsChange = { number ->
                        viewModel.productController.onEvent(ProductEvent.EnteredAmountBs(number))
                    },
                    amountDollar = amountDollar,
                    amountDollarChange = {
                        viewModel.productController.onEvent(
                            ProductEvent.EnteredAmountDollar(
                                it
                            )
                        )
                    },
                    onSave = { viewModel.onEvent(DetailsMarketEvent.SaveProduct) }
                )
                Spacer(modifier = Modifier.widthIn(8.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(TestTags.LIST_PRODUCT)
            ) {
                items(state.list) { product ->

                    SwipeToDismissProduct(
                        id = product.id!!,
                        onDelete = { viewModel.onEvent(DetailsMarketEvent.DeleteProduct(it)) }
                    ) {
                        ItemProduct(
                            item = product,
                            onClick = { viewModel.onEvent(DetailsMarketEvent.UpdateProduct(it)) }
                        )
                    }
                }
            }

        }

    }


}


@ExperimentalMaterialApi
@Composable
fun SwipeToDismissProduct(
    id: Long,
    onDelete: (id: Long) -> Unit,
    content: @Composable () -> Unit
) {

    val state = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onDelete(id)
            }
            true
        }
    )

    SwipeToDismiss(
        state = state,
        background = { BackgroundDismiss(state) },
        dismissContent = { content() },
        directions = setOf(DismissDirection.EndToStart)
    )
    Divider()
}

@ExperimentalMaterialApi
@Composable
fun BackgroundDismiss(state: DismissState) {
    val color = when (state.dismissDirection) {
        DismissDirection.StartToEnd -> Color.Transparent
        DismissDirection.EndToStart -> MaterialTheme.colors.primary
        null -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun ItemProduct(
    onClick: (id: Long) -> Unit,
    item: MarketDetail
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(item.id!!) },

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.quantity.toString(),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.description,
                    modifier = Modifier.weight(3f),
                    style = MaterialTheme.typography.h5
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Por Unidad", modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(
                        id = R.string.string_dollar,
                        item.unitAmountDollar
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(
                        id = R.string.string_bs,
                        item.unitAmount
                    ),

                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            Row {
                Text(text = "Total", modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(
                        id = R.string.string_dollar,
                        item.amountDollar
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = stringResource(
                        id = R.string.string_bs,
                        item.amount
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}


