package com.test.chart

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chart.databinding.WidgetDayChartBinding

class DayChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter() }

    var chartData: List<Item> = emptyList()

    init {
        setupRecycler()
    }

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                reverseLayout = true
            }
        }
        val list = mutableListOf<Item>()
        repeat(10) { list.add(Item(it.toFloat(), "")) }
        chartAdapter.list = list
    }

}