package ve.com.teeac.mymarket.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketDetail(
    val marketId: Long,
    @PrimaryKey
    val id: Long? = null,
    val quantity: Double = 1.0,
    val description: String,
    var unitAmountDollar: Double = 0.00,
    var unitAmount: Double= 0.00,
    var amountDollar: Double = 0.00,
    var amount: Double = 0.00,
    val isActive: Boolean = true
){
    fun addRate(rate: Double): MarketDetail{
        if(rate == 0.00) return this
        if(amount > 0.00 && amountDollar == 0.00){
            amountDollar = amount / rate
            unitAmountDollar = unitAmount / rate
        }else if(amount == 0.00 && amountDollar > 0.00){
            amount = amountDollar * rate
            unitAmount = unitAmountDollar * rate
        }
        return this
    }

}
