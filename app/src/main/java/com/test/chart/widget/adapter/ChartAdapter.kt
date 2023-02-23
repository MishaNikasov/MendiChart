package com.test.chart.widget.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.widget.model.ChartItem
import com.test.chart.widget.utils.ChartUtils
import com.test.chart.widget.adapter.holder.ChartViewHolder
import com.test.chart.widget.adapter.holder.DayViewHolder
import com.test.chart.widget.adapter.holder.MonthViewHolder
import com.test.chart.widget.adapter.holder.SixMonthViewHolder
import com.test.chart.widget.adapter.model.ChartItemWrapper

class ChartAdapter : RecyclerView.Adapter<ChartViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ChartItemWrapper>() {
        override fun areItemsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem.item.id == newItem.item.id
        override fun areContentsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, callback)

    var chartUtils: ChartUtils? = null

    val list: List<ChartItemWrapper>
        get() = differ.currentList

    fun submitList(list: List<ChartItemWrapper>, chartUtils: ChartUtils) {
        differ.submitList(list)
        if (this.chartUtils == null) {
            this.chartUtils = chartUtils
        }
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
        val utils = chartUtils ?: return
        when (holder) {
            is DayViewHolder -> holder.bind(utils, item)
            is MonthViewHolder -> holder.bind(utils, item)
            is SixMonthViewHolder -> holder.bind(utils, item)
        }
    }
}