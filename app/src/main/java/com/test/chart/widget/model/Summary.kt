package com.test.chart.widget.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.test.chart.R
import java.time.LocalDate

sealed class Summary(
    open val neuralActivity: Int,
    open val control: Int,
    open val resilience: Int,
) {

    data class DaySummary(
        val day: LocalDate,
        override val neuralActivity: Int,
        override val control: Int,
        override val resilience: Int
    ) : Summary(neuralActivity, control, resilience)

    data class RangeSummary(
        val range: List<LocalDate>,
        override val neuralActivity: Int,
        override val control: Int,
        override val resilience: Int
    ) : Summary(neuralActivity, control, resilience)

    val textColor: (Context) -> Int = { context ->
        when (this) {
            is DaySummary -> ContextCompat.getColor(context, R.color.summary_day_text_color)
            is RangeSummary -> ContextCompat.getColor(context, R.color.summary_range_text_color)
        }
    }

    val backgroundColor: (Context) -> Int = { context ->
        when (this) {
            is DaySummary -> ContextCompat.getColor(context, R.color.summary_day_bg_color)
            is RangeSummary -> ContextCompat.getColor(context, R.color.summary_range_bg_color)
        }
    }
}