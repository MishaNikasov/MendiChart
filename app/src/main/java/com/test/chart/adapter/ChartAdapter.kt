package com.test.chart.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.chart.ChartItem

class ChartAdapter : RecyclerView.Adapter<ViewHolder>() {
    var list: List<ChartItem> = listOf()
    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ChartItem.DayItem -> DayChartHolder.ITEM_TYPE
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            DayChartHolder.ITEM_TYPE -> DayChartHolder.inflate(parent)
            else -> throw java.lang.Exception()
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is DayChartHolder -> holder.bind(item as ChartItem.DayItem)
        }
    }
}