package com.test.chartlib.widget.adapter.holder

import android.view.ViewGroup
import com.test.chartlib.widget.model.ChartType
import com.test.chartlib.widget.utils.context
import com.test.chartlib.widget.utils.inflater
import com.test.chartlib.widget.utils.px
import com.test.chartlib.R
import com.test.chartlib.databinding.ItemChartBinding
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