package com.test.chart.adapter.holder

import android.view.ViewGroup
import com.test.chart.*
import com.test.chart.databinding.ItemChartBinding
import kotlin.math.roundToInt

class MonthViewHolder(private val binding: ItemChartBinding): ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 2
        fun inflate(parent: ViewGroup) = MonthViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    override fun calculateCellWidth() {
        with(binding) {
            parentLayout.layoutParams = ViewGroup.LayoutParams(context.px(R.dimen.six_month_chart_cell_width).roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            neuralActivity.layoutParams = ViewGroup.LayoutParams(context.px(R.dimen.six_month_chart_item_width).roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            control.layoutParams = ViewGroup.LayoutParams(context.px(R.dimen.six_month_chart_item_width).roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
            resilience.layoutParams = ViewGroup.LayoutParams(context.px(R.dimen.six_month_chart_item_width).roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override val datePattern: String
        get() = "d"

}