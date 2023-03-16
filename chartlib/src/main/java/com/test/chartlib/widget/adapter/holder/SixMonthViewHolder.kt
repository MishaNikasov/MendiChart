package com.test.chartlib.widget.adapter.holder

import android.view.ViewGroup
import com.test.chartlib.databinding.ItemChartBinding
import com.test.chartlib.R
import com.test.chartlib.widget.utils.context
import com.test.chartlib.widget.utils.inflater
import com.test.chartlib.widget.utils.px
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

    override val highlightDate: Boolean
        get() = false
}