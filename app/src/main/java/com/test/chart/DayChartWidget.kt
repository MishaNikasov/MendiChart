package com.test.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chart.adapter.ChartAdapter
import com.test.chart.adapter.ChartItemDecoration
import com.test.chart.databinding.WidgetDayChartBinding

class DayChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter(context) }

    var chartData: List<ChartItem.DayItem> = emptyList()
        set(value) {
            field = value
            refresh()
        }

    init {
        setupRecycler()
        setupLabels()
    }

    private fun setupLabels() {

    }

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                reverseLayout = true
            }
            addItemDecoration(ChartItemDecoration(context))
        }
    }

    private fun refresh() {
        chartAdapter.submitList(chartData)
    }

}