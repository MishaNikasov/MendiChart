package com.test.chart

sealed class ChartItem {

    data class DayItem(
        val neuralActivity: Float,
        val id: String
    ) : ChartItem()

    object Divider : ChartItem()

}
