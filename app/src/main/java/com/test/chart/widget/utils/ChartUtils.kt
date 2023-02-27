package com.test.chart.widget.utils

import android.content.Context
import com.test.chart.R
import com.test.chart.widget.model.*
import com.test.chart.widget.model.ActivityType.*
import kotlin.math.roundToInt

data class ChartUtils(val context: Context, private val rawList: List<ChartItem>) {

    var range: Pair<Int, Int>? = null

    private val rangedList: List<ChartItem>
        get() = range?.let { rawList.subList(it.first, it.second + 1) } ?: emptyList()

    private val maxItemHeight: Float
        get() = context.dp(R.dimen.chart_item_max_height)

    fun calculateCellHeight(value: Float, type: ActivityType): Int {
        if (rangedList.isEmpty()) return 0
        return when(type) {
            NeuralActivity -> dpToPxInt((value / rangedList.maxOf { it.neuralActivity }) * maxItemHeight)
            Control -> dpToPxInt((value / rangedList.maxOf { it.control }) * maxItemHeight)
            Resilience -> dpToPxInt((value / rangedList.maxOf { it.resilience }) * maxItemHeight)
        }
    }

    fun getRangeSummary(): Summary.RangeSummary? {
        if (rangedList.isEmpty()) return null
        return Summary.RangeSummary(
            range = rangedList.map { it.date },
            neuralActivity = rangedList.map { it.neuralActivity }.average().roundToInt(),
            control = rangedList.map { it.control }.average().roundToInt(),
            resilience = rangedList.map { it.resilience }.average().roundToInt()
        )
    }

    fun getAverageValues(): AverageData? {
        range ?: return null
        val averageValueList = rawList.subList(0, range!!.second + 1)
        val neuralAverage = averageValueList.map { it.neuralActivity }.average().toFloat()
        val controlAverage = averageValueList.map { it.control }.average().toFloat()
        val resilienceAverage = averageValueList.map { it.resilience }.average().toFloat()
        return AverageData(
            neuralActivity = AverageItem(
                value = neuralAverage,
                height = calculateCellHeight(neuralAverage, NeuralActivity)
            ),
            control = AverageItem(
                value = controlAverage,
                height = calculateCellHeight(controlAverage, Control)
            ),
            resilience = AverageItem(
                value = resilienceAverage,
                height = calculateCellHeight(resilienceAverage, Resilience)
            )
        )
    }

}