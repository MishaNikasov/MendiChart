package com.test.chart

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup

fun dpToPx(dp: Float): Float = (dp * Resources.getSystem().displayMetrics.density)
fun dpToPxInt(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).toInt()

object ChartUtils {

    fun calculateCellHeight(value: Float): Int {
        val maxItemHeight = 70
        return dpToPxInt((value / maxItemHeight) * maxItemHeight)
    }

}