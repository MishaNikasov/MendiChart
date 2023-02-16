package com.test.chart

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import com.test.chart.ActivityType.*
import java.text.SimpleDateFormat
import java.util.*

data class ChartUtils(val context: Context, private val list: List<ChartItem>) {

    private val neuralActivityMaxValue: Float by lazy { list.maxOf { it.neuralActivity } }
    private val controlMaxValue: Float by lazy { list.maxOf { it.control } }
    private val resilienceMaxValue: Float by lazy { list.maxOf { it.resilience } }

    private val neuralActivityMinValue: Float by lazy { list.minOf { it.neuralActivity } }
    private val controlMinValue: Float by lazy { list.minOf { it.control } }
    private val resilienceMinValue: Float by lazy { list.minOf { it.resilience } }

    fun calculateCellHeight(value: Float, type: ActivityType): Int {
        val maxItemHeight = context.dp(R.dimen.chart_item_max_height)
        return when(type) {
            NeuralActivity -> dpToPxInt((value / neuralActivityMaxValue) * maxItemHeight)
            Control -> dpToPxInt((value / controlMaxValue) * maxItemHeight)
            Resilience -> dpToPxInt((value / resilienceMaxValue) * maxItemHeight)
        }
    }

}