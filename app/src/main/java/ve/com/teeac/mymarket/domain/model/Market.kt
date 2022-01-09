package ve.com.teeac.mymarket.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Market(
    val date: Long = System.currentTimeMillis(),
    val amountDollar: Double = 0.00,
    val amount: Double = 0.00,
    @PrimaryKey val id: Long?= null,
)
