package ve.com.teeac.mymarket.utils

import java.text.SimpleDateFormat

fun getDate(milliseconds: Long): String{
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    return  simpleDateFormat.format(milliseconds)

}
