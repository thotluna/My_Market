package ve.com.teeac.mymarket.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AmountsSetup(
    val rate: Double = 0.00,
    val maximumAvailableDollar: Double = 0.00,
    val maximumAvailable: Double = 0.00,
    @PrimaryKey var id: Long? = null,
    var marketId: Long? = null,
)
