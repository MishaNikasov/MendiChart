package com.test.chart

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import java.text.SimpleDateFormat
import java.util.*

data class ChartUtils(val context: Context, private val list: List<ChartItem>) {

    private val neuralActivityMaxValue: Float by lazy { list.maxOf { it.neuralActivity } }
    private val controlMaxValue: Float by lazy { list.maxOf { it.control } }
    private val resilienceMaxValue: Float by lazy { list.maxOf { it.resilience } }


    fun calculateCellHeight(type: ChartType): Int {
        val maxItemHeight = context.dp(R.dimen.chart_item_max_height)
        return when(type) {
            is ChartType.NeuralActivity -> {
                dpToPxInt((type.value / neuralActivityMaxValue) * maxItemHeight)
            }
            is ChartType.Control -> {
                dpToPxInt((type.value / controlMaxValue) * maxItemHeight)
            }
            is ChartType.Resilience -> {
                dpToPxInt((type.value / resilienceMaxValue) * maxItemHeight)
            }
        }
    }

}