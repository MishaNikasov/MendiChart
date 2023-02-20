package com.test.chart.widget

import java.time.LocalDate

sealed class ChartItem(
    open val id: String,
    open val date: LocalDate,
    open val neuralActivity: Float,
    open val control: Float,
    open val resilience: Float
) {

    data class DayItem(
        override val id: String,
        override val date: LocalDate,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

    data class MonthItem(
        override val id: String,
        override val date: LocalDate,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

    data class SixMonthItem(
        override val id: String,
        override val date: LocalDate,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float
    ) : ChartItem(id, date, neuralActivity, control, resilience)

}
