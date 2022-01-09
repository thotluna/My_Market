package ve.com.teeac.mymarket.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ve.com.teeac.mymarket.R
import ve.com.teeac.mymarket.presentation.components.MyMarketApp

@Preview
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    MyMarketApp {
        Scaffold(
            topBar = { TopMainBar() }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ){
                ListMarkets(
                    listMarkets = viewModel.listMarket,
                )
                Row(

                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    AddMarket()
                }
            }
        }
    }
}

@Composable
fun TopMainBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
    )
}



