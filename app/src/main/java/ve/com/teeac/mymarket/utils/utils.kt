package ve.com.teeac.mymarket.utils

import java.text.SimpleDateFormat
import java.util.*

fun getDate(milliseconds: Long): String{
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return  simpleDateFormat.format(milliseconds)

}
