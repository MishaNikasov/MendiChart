package com.test.chart

import java.util.*

sealed class ChartItem(
    open val id: String,
    open val date: Date,
    open val neuralActivity: Float,
    open val control: Float,
    open val resilience: Float
) {

    data class DayItem(
        override val id: String,
        override val date: Date,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

    data class MonthItem(
        override val id: String,
        override val date: Date,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

    data class SixMonthItem(
        override val id: String,
        override val date: Date,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

}
