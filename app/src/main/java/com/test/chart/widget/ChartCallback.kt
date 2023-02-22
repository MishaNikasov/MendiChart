package com.test.chart.widget

import com.test.chart.widget.adapter.model.ChartItem

interface ChartCallback {
    fun selectChartItem(item: ChartItem)
    fun selectedChartItemList(listItem: List<ChartItem>)
    fun clearSelection()
}