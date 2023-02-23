package com.test.chart.widget.adapter.model

import com.test.chart.widget.model.ChartItem

data class ChartItemWrapper(
    val item: ChartItem,
    val focusState: FocusState
)