package com.test.chart.widget

import android.content.Context
import com.test.chart.R
import com.test.chart.dp
import com.test.chart.dpToPxInt
import com.test.chart.widget.ActivityType.*
import com.test.chart.widget.adapter.model.ChartItem
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

    fun getSummary(): RangeSummary {
        return RangeSummary(
            items = list,
            neuralActivity = list.map { it.neuralActivity }.average().roundToInt(),
            control = list.map { it.control }.average().roundToInt(),
            resilience = list.map { it.resilience }.average().roundToInt()
        )
    }

}