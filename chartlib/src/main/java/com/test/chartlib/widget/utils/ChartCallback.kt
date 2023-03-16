package com.test.chartlib.widget.utils

import com.test.chartlib.widget.model.ChartItem
import com.test.chartlib.widget.model.Summary

interface ChartCallback {
    fun selectChartItem(item: ChartItem)
    fun summary(summary: Summary)
}