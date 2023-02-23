package com.test.chart.widget.summary

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.test.chart.R
import com.test.chart.databinding.WidgetActivitySummaryBinding
import com.test.chart.widget.model.ActivityType
import com.test.chart.widget.model.Summary
import com.test.chart.widget.utils.byPattern
import com.test.chart.widget.utils.dp

class WidgetSummary @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private val binding = WidgetActivitySummaryBinding.inflate(LayoutInflater.from(context), this)

    var data: Summary = Summary.RangeSummary(emptyList(), 0, 0, 0)
        set(value) {
            field = value
            refresh()
        }

    init {
        setPadding(16.dp(), 8.dp(), 16.dp(), 8.dp())
        background = ContextCompat.getDrawable(context, R.drawable.round_background)
        backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.summary_range_bg_color))
    }

    @SuppressLint("SetTextI18n")
    private fun refresh() {
        with(binding) {
            val textColor = data.textColor(context)
            with(neuralActivityItem) {
                val labelColor = when(data) {
                    is Summary.DaySummary -> ActivityType.NeuralActivity.brightTextColor(context)
                    is Summary.RangeSummary -> ActivityType.NeuralActivity.textColor(context)
                }
                label.text = ActivityType.NeuralActivity.title(context)
                label.setTextColor(labelColor)
                value.text = "+${data.neuralActivity}%"
                value.setTextColor(textColor)
            }
            with(controlItem) {
                val labelColor = when(data) {
                    is Summary.DaySummary -> ActivityType.Control.brightTextColor(context)
                    is Summary.RangeSummary -> ActivityType.Control.textColor(context)
                }
                label.text = ActivityType.Control.title(context)
                label.setTextColor(labelColor)
                value.text = "${data.control}s"
                value.setTextColor(textColor)
            }
            with(resilienceItem) {
                val labelColor = when(data) {
                    is Summary.DaySummary -> ActivityType.Resilience.brightTextColor(context)
                    is Summary.RangeSummary -> ActivityType.Resilience.textColor(context)
                }
                label.text = ActivityType.Resilience.title(context)
                label.setTextColor(labelColor)
                value.text = "${data.resilience}p"
                value.setTextColor(textColor)
            }
            with(daySummary) {
                val itemListText = when(data) {
                    is Summary.DaySummary -> {
                        (data as Summary.DaySummary).day.byPattern("E d MMM yyyy")
                    }
                    is Summary.RangeSummary -> {
                        val dates = (data as Summary.RangeSummary).range
                        "${dates[0].byPattern("d, MMM")} - ${dates[dates.lastIndex].byPattern("d, MMM")}"
                    }
                }
                text = itemListText
                setTextColor(textColor)
            }
            summ.setTextColor(textColor)
            backgroundTintList = ColorStateList.valueOf(data.backgroundColor(context))
        }
    }

}