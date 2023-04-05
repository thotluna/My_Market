package ve.com.teeac.mymarket.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun getDate(milliseconds: Long): String{
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return  simpleDateFormat.format(milliseconds)
}

fun roundOffDecimal(number: Double): Double {
    return BigDecimal(number).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

fun formatBolivares(bolivares: Double): String {
    val paisActual = Locale(Locale.getDefault().language, Locale.getDefault().isO3Country)
    val nf = NumberFormat.getCurrencyInstance(paisActual)

    return "${nf.format(bolivares)} Bs"
}

fun formatDollar(dollar: Double): String {
    val paisActual = Locale(Locale.getDefault().language, "US")
    val nf = NumberFormat.getCurrencyInstance(paisActual)

    return "${nf.format(dollar)} "
}
