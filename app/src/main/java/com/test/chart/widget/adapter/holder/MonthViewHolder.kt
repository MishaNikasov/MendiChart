package com.test.chart.widget.adapter.holder

import android.view.ViewGroup
import com.test.chart.*
import com.test.chart.databinding.ItemChartBinding
import com.test.chart.widget.model.ChartType
import com.test.chart.widget.utils.context
import com.test.chart.widget.utils.inflater
import com.test.chart.widget.utils.px
import kotlin.math.roundToInt

class MonthViewHolder(private val binding: ItemChartBinding): ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 2
        fun inflate(parent: ViewGroup) = MonthViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false))
    }

    override val cellWidth: Int
        get() = (context.resources.displayMetrics.widthPixels - (context.px(R.dimen.chart_left_margin).roundToInt() + context.px(R.dimen.chart_right_margin).roundToInt())) / ChartType.Month.itemsRange

    override val itemWidth: Int
        get() = (cellWidth * 0.7).roundToInt()

    override val datePattern: String
        get() = "d"

    override val dateTextSize: Float
        get() = 10f

    override val highlightDate: Boolean
        get() = false

}