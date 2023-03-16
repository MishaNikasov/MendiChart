package com.test.chartlib.widget.adapter.holder

import android.view.ViewGroup
import com.test.chartlib.databinding.ItemChartBinding
import com.test.chartlib.R
import com.test.chartlib.widget.model.ChartType
import com.test.chartlib.widget.utils.context
import com.test.chartlib.widget.utils.inflater
import com.test.chartlib.widget.utils.px
import kotlin.math.roundToInt

class DayViewHolder(private val binding: ItemChartBinding, private val parentWidth: Int) : ChartViewHolder(binding) {

    companion object {
        val ITEM_TYPE = R.layout.item_chart + 1
        fun inflate(parent: ViewGroup) = DayViewHolder(ItemChartBinding.inflate(parent.inflater, parent, false), parent.width)
    }

    override val cellWidth: Int
        get() = (context.resources.displayMetrics.widthPixels - (context.px(R.dimen.chart_left_margin).roundToInt() + context.px(R.dimen.chart_right_margin).roundToInt())) / ChartType.Week.itemsRange

    override val itemWidth: Int
        get() = (cellWidth * 0.7).roundToInt()

    override val datePattern: String
        get() = "EEE"

    override val dateTextSize: Float
        get() = 12f

    override val highlightDate: Boolean
        get() = true

}