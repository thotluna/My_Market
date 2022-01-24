package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupEvent
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupForm
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupState
import ve.com.teeac.mymarket.presentation.marketdetails.details_market.ItemProduct
import ve.com.teeac.mymarket.presentation.marketdetails.details_market.SectionTotals
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

    val context = LocalContext.current

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
                    Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${context.getString(R.string.app_name)} ${viewModel.state.value.marketId}")
                }

            }

        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(modifier = Modifier
                    .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    IconButton(
                        onClick = { navigateUp() },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.button_back)
                        )
                    }
                    Text(text = "Atras")
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    IconButton(
                        onClick = {
                            viewModel.setupController.onToggleSection()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.button_open_hide_setup_section)
                        )
                    }
                    Text(text = "Setup")
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    IconButton(
                        onClick = {
                            viewModel.productController.onToggleSection()
                            viewModel.onEvent(DetailsMarketEvent.ClearProductForm)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = stringResource(R.string.button_open_product_section)
                        )
                    }
                    Text(text = "Productos")
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
                    onSave = { viewModel.onEvent(DetailsMarketEvent.SaveProduct) },
                    persistent = viewModel.productController.persistentShowSection.value,
                    changePersistent = { viewModel.productController.onChangedPersistent() }
                )
                Spacer(modifier = Modifier.widthIn(8.dp))
            }
            SectionTotals(
                currentDollars = viewModel.totalDollar.value,
                currentBolivares = viewModel.totalBolivares.value,
                availableBolivares = viewModel.availableBolivares.value,
                availableDollars = viewModel.availableDollars.value
            )
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
                            onClick = { viewModel.onEvent(DetailsMarketEvent.UpdateProduct(it)) },
                            onChecked = {id, value ->
                                viewModel.onEvent(DetailsMarketEvent.ChangedActivatedProduct(id, value))
                            }
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




