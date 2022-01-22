package ve.com.teeac.mymarket.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun getDate(milliseconds: Long): String{
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return  simpleDateFormat.format(milliseconds)
}

fun roundOffDecimal(number: Double): Double? {
//    val df = DecimalFormat("#.##")
//    df.roundingMode = RoundingMode.FLOOR
//    return df.format(number).toDouble()
    return BigDecimal(number).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}
