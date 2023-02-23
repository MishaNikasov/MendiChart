package com.test.chart.widget

import com.test.chart.widget.adapter.model.ChartItem

data class RangeSummary(
    val items: List<ChartItem>,
    val neuralActivity: Int,
    val control: Int,
    val resilience: Int,
)