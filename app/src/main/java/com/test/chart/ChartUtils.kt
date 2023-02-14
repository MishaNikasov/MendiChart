package com.test.chart

import android.content.res.Resources

fun dpToPx(dp: Float): Float = (dp * Resources.getSystem().displayMetrics.density)
fun dpToPxInt(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

object ChartUtils {

    fun calculateCellHeight(value: Float): Int {
        val maxItemHeight = 70
        return dpToPxInt((value / maxItemHeight) * maxItemHeight)
    }

}