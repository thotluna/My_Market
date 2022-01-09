package ve.com.teeac.mymarket.presentation.delete

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ve.com.teeac.mymarket.presentation.components.MyMarketApp

@Preview
@Composable
fun DeleteScreen() {
    var conversor by remember{ mutableStateOf("0.00") }
    MyMarketApp {
        Scaffold(
            topBar = {
                TopAppBar() {
                    Text(text = "My App")
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        ) {
            Box(modifier = Modifier
                .padding(16.dp)){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        TextField(
                            value = conversor,
                            onValueChange = { conversor = it },
                            modifier = Modifier.weight(1f),
                            label = { Text(text = "Taza") }
                        )
                        TextField(
                            value = conversor,
                            onValueChange = { conversor = it },
                            modifier = Modifier.weight(1f),
                            label = { Text(text = "$") }
                        )
                        TextField(
                            value = conversor,
                            onValueChange = { conversor = it },
                            modifier = Modifier.weight(1f),
                            label = { Text(text = "Bs") }
                        )

                    }

                }
            }

        }
    }
}