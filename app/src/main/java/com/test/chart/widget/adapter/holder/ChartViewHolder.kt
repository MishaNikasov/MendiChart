package com.test.chart.widget.adapter.holder

import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.*
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import com.test.chart.databinding.ItemChartBinding
import com.test.chart.widget.ActivityType
import com.test.chart.widget.ChartItem
import com.test.chart.widget.ChartUtils

abstract class ChartViewHolder(private val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root) {

    abstract fun calculateCellWidth()

    abstract val datePattern: String

    fun bind(
        chartUtils: ChartUtils,
        wrapper: ChartItemWrapper,
        selectListener: (ChartItem) -> Unit
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
            bottomDate.text = dateText
            topDate.text = dateText
            when (wrapper.focusState) {
                FocusState.Preview -> {
                    neuralActivity.backgroundTintList = ColorStateList.valueOf(ActivityType.NeuralActivity.selectedColor(context))
                    control.backgroundTintList = ColorStateList.valueOf(ActivityType.Control.selectedColor(context))
                    resilience.backgroundTintList = ColorStateList.valueOf(ActivityType.Resilience.selectedColor(context))
                    selectedOverlay.isInvisible = true
                }
                FocusState.InFocus -> {
                    neuralActivity.backgroundTintList = ColorStateList.valueOf(ActivityType.NeuralActivity.selectedColor(context))
                    control.backgroundTintList = ColorStateList.valueOf(ActivityType.Control.selectedColor(context))
                    resilience.backgroundTintList = ColorStateList.valueOf(ActivityType.Resilience.selectedColor(context))
                    selectedOverlay.isVisible = true
                }
                FocusState.OutOfFocus -> {
                    neuralActivity.backgroundTintList = ColorStateList.valueOf(ActivityType.NeuralActivity.unselectedColor(context))
                    control.backgroundTintList = ColorStateList.valueOf(ActivityType.Control.unselectedColor(context))
                    resilience.backgroundTintList = ColorStateList.valueOf(ActivityType.Resilience.unselectedColor(context))
                    selectedOverlay.isInvisible = true
                }
            }
            root.setOnClickListener { selectListener(item) }
        }
    }

}