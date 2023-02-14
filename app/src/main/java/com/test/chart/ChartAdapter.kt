package com.test.chart

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.databinding.ItemChartBinding

class ChartAdapter : RecyclerView.Adapter<ChartHolder>() {
    var list: List<Item> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder {
        return ChartHolder(ItemChartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ChartHolder, position: Int) {
        holder.bind(list[position])
    }
}

class ChartHolder(private val binding: ItemChartBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item) {

        with(binding) {
            val neuralActivityCellLp = neuralActivity.layoutParams
            neuralActivity.layoutParams = FrameLayout.LayoutParams(
                neuralActivityCellLp.width,
                ChartUtils.calculateCellHeight(item.neuralActivity),
                Gravity.BOTTOM
            )
        }
    }
}
