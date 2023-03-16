package com.test.chart.widget.model

enum class ChartType {
    Week,
    Month,
    SixMonth;

    val itemsRange: Int
        get() {
            return when(this) {
                Week -> 7
                Month -> 30
                SixMonth -> 31
            }
        }
}