package com.test.chart.widget.utils

import com.test.chart.widget.model.ChartItem
import com.test.chart.widget.model.Summary

interface ChartCallback {
    fun selectChartItem(item: ChartItem)
    fun summary(summary: Summary)
}