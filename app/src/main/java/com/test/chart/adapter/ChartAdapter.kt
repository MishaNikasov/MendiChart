package com.test.chart.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.ChartItem
import com.test.chart.ChartUtils
import com.test.chart.adapter.holder.ChartViewHolder
import com.test.chart.adapter.holder.DayViewHolder
import com.test.chart.adapter.holder.MonthViewHolder
import com.test.chart.adapter.holder.SixMonthViewHolder
import com.test.chart.adapter.model.ChartItemWrapper

class ChartAdapter(private val context: Context) : RecyclerView.Adapter<ChartViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ChartItemWrapper>() {
        override fun areItemsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem.item.id == newItem.item.id
        override fun areContentsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, callback)

    lateinit var chartUtils: ChartUtils

    var selectListener: (ChartItem) -> Unit = { }

    val list: List<ChartItemWrapper>
        get() = differ.currentList

    fun submitList(list: List<ChartItemWrapper>?) {
        if (list == null) return
        differ.submitList(list)
        chartUtils = ChartUtils(context, list.map { it.item })
    }

    fun getItem(position: Int) = list[position]

    override fun getItemViewType(position: Int): Int {
        return when (list[position].item) {
            is ChartItem.DayItem -> DayViewHolder.ITEM_TYPE
            is ChartItem.MonthItem -> MonthViewHolder.ITEM_TYPE
            is ChartItem.SixMonthItem -> SixMonthViewHolder.ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return when (viewType) {
            DayViewHolder.ITEM_TYPE -> DayViewHolder.inflate(parent)
            MonthViewHolder.ITEM_TYPE -> MonthViewHolder.inflate(parent)
            SixMonthViewHolder.ITEM_TYPE -> SixMonthViewHolder.inflate(parent)
            else -> throw java.lang.Exception()
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is DayViewHolder -> holder.bind(chartUtils, item, selectListener)
            is MonthViewHolder -> holder.bind(chartUtils, item, selectListener)
            is SixMonthViewHolder -> holder.bind(chartUtils, item, selectListener)
        }
    }
}