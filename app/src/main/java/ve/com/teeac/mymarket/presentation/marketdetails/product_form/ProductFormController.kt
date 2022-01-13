package ve.com.teeac.mymarket.presentation.marketdetails.product_form

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ve.com.teeac.mymarket.domain.model.MarketDetail
import ve.com.teeac.mymarket.presentation.marketdetails.NoteTextFieldState
import ve.com.teeac.mymarket.presentation.marketdetails.NumberTextFieldState

class ProductFormController {

    private val _rate = mutableStateOf<Number>(0)
    val rate: State<Number?> = _rate

    private val _id = mutableStateOf<Long?>(null)
    val id: State<Long?> = _id

    private val _quantity = mutableStateOf(
        NumberTextFieldState(
            title = "Cantidad"
        )
    )
    val quantity: State<NumberTextFieldState> = _quantity

    private val _description = mutableStateOf(
        NoteTextFieldState(
            hint = "Descripción"
        )
    )
    val description: State<NoteTextFieldState> = _description

    private val _amountDollar = mutableStateOf(
        NumberTextFieldState(
            title = "$"
        )
    )
    val amountDollar: State<NumberTextFieldState> = _amountDollar

    private val _amountBs = mutableStateOf(
        NumberTextFieldState(
            title = "Bs"
        )
    )
    val amountBs: State<NumberTextFieldState> = _amountBs

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.EnteredQuantity -> {
                if (event.value == quantity.value.number) return
                _quantity.value = quantity.value.copy(number = event.value)
            }
            is ProductEvent.EnteredDescription -> {
                if (event.value == description.value.text) return
                _description.value = description.value.copy(text = event.value)
            }
            is ProductEvent.EnteredAmountBs -> {
                if (event.value == amountBs.value.number) return
                _amountBs.value = amountBs.value.copy(number = event.value)
                if(_rate.value.toDouble() > 0){
                    _amountDollar.value = amountDollar.value.copy(
                        number = amountBs.value.number!!.toDouble() / _rate.value.toDouble()
                    )
                }
            }
            is ProductEvent.EnteredAmountDollar -> {
                if (event.value == amountDollar.value.number) return
                _amountDollar.value = amountDollar.value.copy(number = event.value)
                _amountBs.value = amountBs.value.copy(
                    number = amountDollar.value.number!!.toDouble() * _rate.value.toDouble()
                )
            }
            is ProductEvent.UpdateProduct -> {
                _id.value = event.product.id
                _quantity.value = quantity.value.copy(number = event.product.quantity)
                _description.value = description.value.copy(text = event.product.description)
                _amountDollar.value =
                    amountDollar.value.copy(number = event.product.unitAmountDollar)
                _amountBs.value = amountBs.value.copy(number = event.product.unitAmount)
            }
            is ProductEvent.UpdateRate -> {
                _rate.value = event.rate
            }
        }
    }

    fun getMarketDetail(marketId: Long): MarketDetail {
        return MarketDetail(
            id = id.value,
            marketId = marketId,
            quantity = quantity.value.number?.toDouble() ?: 1.0,
            description = description.value.text ?: "",
            unitAmount = amountBs.value.number?.toDouble() ?: 0.0,
            unitAmountDollar = amountDollar.value.number?.toDouble() ?: 0.00,
            amount = getAmountBsTotal(),
            amountDollar = getAmountDollarTotal(),
        )
    }

    private fun getAmountBsTotal(): Double {
        return quantity.value.number?.let { quantity ->
            if (amountBs.value.number != null) {
                amountBs.value.number!!.toDouble() * quantity.toDouble()
            } else {
                0.00
            }
        } ?: 0.0
    }

    private fun getAmountDollarTotal(): Double {
        return quantity.value.number?.let { quantity ->
            if (amountDollar.value.number != null) {
                _amountDollar.value.number!!.toDouble() * quantity.toDouble()
            } else {
                0.00
            }
        } ?: 0.0
    }

    fun clear() {
        _id.value = null
        _quantity.value = quantity.value.copy(number = null)
        _description.value = description.value.copy(text = null)
        _amountBs.value = amountBs.value.copy(number = null)
        _amountDollar.value = amountDollar.value.copy(number = null)
    }


}
