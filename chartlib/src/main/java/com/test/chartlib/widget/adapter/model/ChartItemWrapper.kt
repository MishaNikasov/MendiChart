package com.test.chartlib.widget.adapter.model

import com.test.chartlib.widget.model.ChartItem

data class ChartItemWrapper(
    val item: ChartItem,
    val focusState: FocusState
)