package com.test.chart.widget.adapter.holder

import android.view.ViewGroup
import com.test.chart.*
import com.test.chart.databinding.ItemChartBinding
import kotlin.math.roundToInt

class SixMonthViewHolder(private val binding: ItemChartBinding): ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 3
        fun inflate(parent: ViewGroup) = SixMonthViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    override val cellWidth: Int
        get() = context.px(R.dimen.six_month_chart_cell_width).roundToInt()

    override val itemWidth: Int
        get() = context.px(R.dimen.six_month_chart_item_width).roundToInt()

    override val datePattern: String
        get() = ""

    override val dateTextSize: Float
        get() = 10f

}