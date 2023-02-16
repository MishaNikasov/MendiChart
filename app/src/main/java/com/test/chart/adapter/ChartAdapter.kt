package com.test.chart.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.test.chart.ChartItem
import com.test.chart.ChartUtils

class ChartAdapter(private val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ChartItem>() {
        override fun areItemsTheSame(oldItem: ChartItem, newItem: ChartItem) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ChartItem, newItem: ChartItem) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, callback)

    lateinit var chartUtils: ChartUtils

    val list: List<ChartItem>
        get() = differ.currentList

    fun submitList(list: List<ChartItem>?) {
        if (list == null) return
        differ.submitList(list)
        chartUtils = ChartUtils(context, list)
    }

    fun getItem(position: Int) = list[position]

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ChartItem.DayItem -> {
                DayChartHolder.ITEM_TYPE
            }
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            DayChartHolder.ITEM_TYPE -> {
                DayChartHolder.inflate(parent)
            }
            else -> throw java.lang.Exception()
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is DayChartHolder -> holder.bind(
                chartUtils,
                item as ChartItem.DayItem,
            )
        }
    }
}