package com.test.chartlib.widget.model

import java.time.LocalDate

sealed class ChartItem(
    open val id: String,
    open val date: LocalDate,
    open val score: Int,
    open val neuralActivity: Float,
    open val control: Float,
    open val resilience: Float,
    open val isPlaceholder: Boolean
) {

    data class DayItem(
        override val id: String,
        override val date: LocalDate,
        override val score: Int,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float,
        override val isPlaceholder: Boolean = false
    ) : ChartItem(id, date, score, neuralActivity, control, resilience, isPlaceholder)

    data class MonthItem(
        override val id: String,
        override val date: LocalDate,
        override val score: Int,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float,
        override val isPlaceholder: Boolean = false
    ) : ChartItem(id, date, score, neuralActivity, control, resilience, isPlaceholder)

    data class SixMonthItem(
        override val id: String,
        override val date: LocalDate,
        override val score: Int,
        override val neuralActivity: Float,
        override val control: Float,
        override val resilience: Float,
        override val isPlaceholder: Boolean = false
    ) : ChartItem(id, date, score, neuralActivity, control, resilience, isPlaceholder)

}
