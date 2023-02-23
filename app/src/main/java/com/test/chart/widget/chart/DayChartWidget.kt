package com.test.chart.widget.chart

import android.content.Context
import android.util.AttributeSet
import com.test.chart.widget.ChartType
import com.test.chart.widget.adapter.model.ChartItem
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState

class DayChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ChartWidget(context, attrs, defStyle) {

    override val itemsRange: Int
        get() = ChartType.Days.itemsRange

    var dayList: List<ChartItem.DayItem> = emptyList()
        set(value) {
            field = value
            chartData = value.map { ChartItemWrapper(it, FocusState.Preview) }
        }

}