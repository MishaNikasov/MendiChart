package com.test.chart

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import java.text.SimpleDateFormat
import java.util.*

fun dpToPx(dp: Float): Float = (dp * Resources.getSystem().displayMetrics.density)
fun dpToPxInt(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()
fun generateId(): String = UUID.randomUUID().toString()
fun pxToDpInt(px: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics).toInt()
fun pxToDp(px: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics)

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.sp() = (this * Resources.getSystem().displayMetrics.scaledDensity)

fun Date.byPattern(
    pattern: String,
    locale: Locale = Locale.getDefault()
): String {
    val formattedDate: String

    val simpleDateFormat = SimpleDateFormat(pattern, locale)
    formattedDate = simpleDateFormat.format(this)

    return formattedDate
}


fun Context.px(@DimenRes dimen: Int): Float = resources.getDimension(dimen)

fun Context.dp(@DimenRes dimen: Int): Float = px(dimen) / resources.displayMetrics.density