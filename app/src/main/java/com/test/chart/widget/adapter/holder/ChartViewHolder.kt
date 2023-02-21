package com.test.chart.widget.adapter.holder

import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.byPattern
import com.test.chart.context
import com.test.chart.databinding.ItemChartBinding
import com.test.chart.widget.ActivityType
import com.test.chart.widget.ChartUtils
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState

abstract class ChartViewHolder(private val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun calculateCellWidth()

    abstract val datePattern: String

    abstract val dateTextSize: Float

    fun bind(
        chartUtils: ChartUtils,
        wrapper: ChartItemWrapper
    ) {
        with(binding) {
            calculateCellWidth()
            val neuralActivityCellLp = neuralActivity.layoutParams
            val item = wrapper.item
            neuralActivity.layoutParams = FrameLayout.LayoutParams(
                neuralActivityCellLp.width,
                chartUtils.calculateCellHeight(item.neuralActivity, ActivityType.NeuralActivity),
                Gravity.BOTTOM or Gravity.CENTER
            )
            val controlCellLp = control.layoutParams
            control.layoutParams = FrameLayout.LayoutParams(
                controlCellLp.width,
                chartUtils.calculateCellHeight(item.control, ActivityType.Control),
                Gravity.BOTTOM or Gravity.CENTER
            )
            val resilienceCellLp = resilience.layoutParams
            resilience.layoutParams = FrameLayout.LayoutParams(
                resilienceCellLp.width,
                chartUtils.calculateCellHeight(item.resilience, ActivityType.Resilience),
                Gravity.BOTTOM or Gravity.CENTER
            )
            val dateText = item.date.byPattern(datePattern)
            with(topDate) {
                text = dateText
                textSize = dateTextSize
            }
            with(bottomDate) {
                text = dateText
                textSize = dateTextSize
            }
            when (wrapper.focusState) {
                FocusState.Preview -> {
                    neuralActivity.background = ActivityType.NeuralActivity.selectedBg(context)
                    control.background = ActivityType.Control.selectedBg(context)
                    resilience.background = ActivityType.Resilience.selectedBg(context)
                    selectedOverlay.isInvisible = true
                }
                FocusState.InFocus -> {
                    neuralActivity.background = ActivityType.NeuralActivity.selectedBg(context)
                    control.background = ActivityType.Control.selectedBg(context)
                    resilience.background = ActivityType.Resilience.selectedBg(context)
                    selectedOverlay.isVisible = true
                }
                FocusState.OutOfFocus -> {
                    neuralActivity.background = ActivityType.NeuralActivity.unselectedBg(context)
                    control.background = ActivityType.Control.unselectedBg(context)
                    resilience.background = ActivityType.Resilience.unselectedBg(context)
                    selectedOverlay.isInvisible = true
                }
            }
        }
    }

}