package ve.com.teeac.mymarket.ui.market.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormProduct() {
    var quantity by rememberSaveable { mutableStateOf<Number?>(null) }
    var description by rememberSaveable { mutableStateOf("") }
    var dollar by rememberSaveable { mutableStateOf<Number?>(null) }
    var bs by rememberSaveable { mutableStateOf<Number?>(null) }

    CardMarket {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)


        ) {
            NumberField(
                value = quantity,
                onNumberChange = { quantity = it },
                name = "Cantidad",
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = "Descripcion") },
                placeholder = { Text(text = "Descripcion") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberField(
                    value = dollar,
                    onNumberChange = { dollar = it },
                    name = "$",
                    modifier = Modifier
                        .weight(1f)
                )
                NumberField(
                    value = bs,
                    onNumberChange = { bs = it },
                    name = "Bs",
                    modifier = Modifier
                        .weight(1f)
                )
                Button(
                    onClick = {
                        quantity = null
                        description = ""
                        dollar = null
                        bs = null
                    },
                    contentPadding = PaddingValues(4.dp, 18.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Like")
                }
            }
        }
    }
}