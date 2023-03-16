package com.test.chartlib.widget.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.chartlib.widget.adapter.holder.ChartViewHolder
import com.test.chartlib.widget.adapter.holder.DayViewHolder
import com.test.chartlib.widget.adapter.holder.MonthViewHolder
import com.test.chartlib.widget.adapter.holder.SixMonthViewHolder
import com.test.chartlib.widget.adapter.model.ChartItemWrapper
import com.test.chartlib.widget.model.ChartType
import com.test.chartlib.widget.utils.ChartUtils

class ChartAdapter(private val context: Context, private val chartType: ChartType) : RecyclerView.Adapter<ChartViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ChartItemWrapper>() {
        override fun areItemsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem.item.id == newItem.item.id
        override fun areContentsTheSame(oldItem: ChartItemWrapper, newItem: ChartItemWrapper) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, callback)

    private var chartUtils: ChartUtils? = null

    val list: List<ChartItemWrapper>
        get() = differ.currentList

    fun submitList(list: List<ChartItemWrapper>) {
        differ.submitList(list)
        if (chartUtils == null) {
            this.chartUtils = ChartUtils(context, list.map { it.item })
        }
    }

    fun setCurrentCalculationRange(from: Int, to: Int) {
        chartUtils?.range = Pair(from, to)
    }

    fun getSummary() = chartUtils?.getRangeSummary()

    fun getAverages() = chartUtils?.getAverageValues()

    fun getItem(position: Int) = list[position]

    override fun getItemViewType(position: Int): Int {
        return when (chartType) {
            ChartType.Week -> DayViewHolder.ITEM_TYPE
            ChartType.Month -> MonthViewHolder.ITEM_TYPE
            ChartType.SixMonth -> SixMonthViewHolder.ITEM_TYPE
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