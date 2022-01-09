package ve.com.teeac.mymarket.presentation.marketdetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.mymarket.presentation.components.FloatingButton
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupEvent
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupForm
import ve.com.teeac.mymarket.presentation.marketdetails.amountssetup.AmountSetupState


@Composable
fun DetailsMarketScreen(
    navigateUp: () -> Unit,
    viewModel: DetailsMarketViewModel = hiltViewModel()
) {
    val converterState = viewModel.statesAmountsSetup.rate.value
    val amountState = viewModel.statesAmountsSetup.maxBolivares.value
    val amountsDollarState = viewModel.statesAmountsSetup.maxDollar.value

    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(
                    onClick = { navigateUp() },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Mercado")
            }
        },
        floatingActionButton = {
            FloatingButton(onNew = {
                /* TODO implement */
            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
        ) {
            AmountSetupForm(
                converterState = AmountSetupState(
                    converterState,
                    onChange = {
                        viewModel.statesAmountsSetup
                            .onAmountsSetupEvent(AmountSetupEvent.EnteredConvert(it))
                    }
                ),
                amountState = AmountSetupState(
                    field = amountState,
                    onChange = {
                        viewModel.statesAmountsSetup
                            .onAmountsSetupEvent(AmountSetupEvent.EnteredAmounts(it))
                    }
                ),
                amountsDollarState = AmountSetupState(
                    field = amountsDollarState,
                    onChange = {
                        viewModel.statesAmountsSetup
                            .onAmountsSetupEvent(AmountSetupEvent.EnteredAmountsDollar(it))
                    }
                )
            )
            Spacer(modifier = Modifier.widthIn(8.dp))

        }

    }


}



