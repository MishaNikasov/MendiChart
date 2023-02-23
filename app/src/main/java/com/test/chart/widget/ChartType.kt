package com.test.chart.widget

enum class ChartType {
    Days,
    Weeks,
    SixWeeks;

    val itemsRange: Int
        get() {
            return when(this) {
                Days -> 7
                Weeks -> 30
                SixWeeks -> 31
            }
        }
}