package com.test.chart.widget.utils

import android.content.Context
import com.test.chart.R
import com.test.chart.widget.model.ActivityType.*
import com.test.chart.widget.model.ChartItem
import com.test.chart.widget.model.ActivityType
import com.test.chart.widget.model.Summary
import kotlin.math.roundToInt

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

    fun getRangeSummary(): Summary.RangeSummary {
        return Summary.RangeSummary(
            range = list.map { it.date },
            neuralActivity = list.map { it.neuralActivity }.average().roundToInt(),
            control = list.map { it.control }.average().roundToInt(),
            resilience = list.map { it.resilience }.average().roundToInt()
        )
    }

}