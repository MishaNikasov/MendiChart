package com.test.chart.adapter.model

import com.test.chart.ChartItem

data class ChartItemWrapper(
    val item: ChartItem,
    val focusState: FocusState
)