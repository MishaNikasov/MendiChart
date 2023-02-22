package com.test.chart.widget.adapter.holder

import android.view.ViewGroup
import com.test.chart.*
import com.test.chart.databinding.ItemChartBinding
import kotlin.math.roundToInt

class MonthViewHolder(private val binding: ItemChartBinding): ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 2
        fun inflate(parent: ViewGroup) = MonthViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    override val cellWidth: Int
        get() = context.px(R.dimen.month_chart_cell_width).roundToInt()

    override val itemWidth: Int
        get() = context.px(R.dimen.month_chart_item_width).roundToInt()

    override val datePattern: String
        get() = "d"

    override val dateTextSize: Float
        get() = 10f

}