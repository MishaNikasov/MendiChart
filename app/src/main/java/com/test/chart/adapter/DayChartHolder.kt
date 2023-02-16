package com.test.chart.adapter

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.*
import com.test.chart.databinding.ItemChartBinding

class DayChartHolder(private val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart
        fun inflate(parent: ViewGroup) = DayChartHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    fun bind(
        chartUtils: ChartUtils,
        item: ChartItem.DayItem
    ) {
        with(binding) {
            val neuralActivityCellLp = neuralActivity.layoutParams
            neuralActivity.layoutParams = FrameLayout.LayoutParams(
                neuralActivityCellLp.width,
                chartUtils.calculateCellHeight(ChartType.NeuralActivity(item.neuralActivity)),
                Gravity.BOTTOM or Gravity.CENTER
            )
            val controlCellLp = control.layoutParams
            control.layoutParams = FrameLayout.LayoutParams(
                controlCellLp.width,
                chartUtils.calculateCellHeight(ChartType.Control(item.control)),
                Gravity.BOTTOM or Gravity.CENTER
            )
            val resilienceCellLp = resilience.layoutParams
            resilience.layoutParams = FrameLayout.LayoutParams(
                resilienceCellLp.width,
                chartUtils.calculateCellHeight(ChartType.Resilience(item.resilience)),
                Gravity.BOTTOM or Gravity.CENTER
            )
        }
    }
}