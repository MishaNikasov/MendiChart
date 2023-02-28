package com.test.chart.widget.adapter.holder

import android.view.ViewGroup
import com.test.chart.R
import com.test.chart.databinding.ItemChartBinding
import com.test.chart.widget.model.ChartType
import com.test.chart.widget.utils.context
import com.test.chart.widget.utils.inflater
import com.test.chart.widget.utils.px
import kotlin.math.roundToInt

class DayViewHolder(private val binding: ItemChartBinding, private val parentWidth: Int) : ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 1
        fun inflate(parent: ViewGroup) = DayViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false), parent.width)
    }

    override val cellWidth: Int
        get() =
            (context.resources.displayMetrics.widthPixels -
                    (context.px(R.dimen.chart_left_margin).roundToInt() + context.px(R.dimen.chart_right_margin).roundToInt())) / ChartType.Days.itemsRange

    override val itemWidth: Int
        get() = (cellWidth * 0.7).roundToInt()

    override val datePattern: String
        get() = "EEE"

    override val dateTextSize: Float
        get() = 12f

}