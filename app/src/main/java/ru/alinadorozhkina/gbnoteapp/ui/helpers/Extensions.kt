package ru.alinadorozhkina.gbnoteapp.ui.helpers

import android.content.Context
import androidx.core.content.ContextCompat
import ru.alinadorozhkina.gbnoteapp.R
import ru.alinadorozhkina.gbnoteapp.data.model.models.Color
import java.text.SimpleDateFormat
import java.util.*

const val TIME_DATA_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String = SimpleDateFormat(TIME_DATA_FORMAT, Locale.getDefault()).format(this)

fun Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(context, getColorRes())

fun Color.getColorRes(): Int = when (this) {
    Color.BLUE -> R.color.blue_dark
    Color.ORANGE -> R.color.orange_main
    Color.RED -> R.color.red
    Color.GREEN -> R.color.green
    Color.YELLOW -> R.color.yellow
}

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()