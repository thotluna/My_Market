package ve.com.teeac.mymarket.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketDetail(
    val marketId: Long,
    @PrimaryKey
    val id: Int,
    val quantity: Double = 1.0,
    val description: String,
    val unitAmountDollar: Double = 0.00,
    val unitAmount: Double= 0.00,
    val amountDollar: Double = 0.00,
    val amount: Double = 0.00
)
