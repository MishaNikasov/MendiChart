package com.test.chart.adapter

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.ChartItem
import com.test.chart.ChartUtils
import com.test.chart.R
import com.test.chart.databinding.ItemChartBinding
import com.test.chart.inflater

class DayChartHolder(private val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart
        fun inflate(parent: ViewGroup) = DayChartHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    fun bind(item: ChartItem.DayItem) {
        with(binding) {
            val neuralActivityCellLp = neuralActivity.layoutParams
            neuralActivity.layoutParams = FrameLayout.LayoutParams(
                neuralActivityCellLp.width,
                ChartUtils.calculateCellHeight(item.neuralActivity),
                Gravity.BOTTOM or Gravity.CENTER
            )
        }
    }
}