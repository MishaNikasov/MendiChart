package com.test.chart.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chart.ChartItem
import com.test.chart.R
import com.test.chart.adapter.ChartAdapter
import com.test.chart.adapter.ChartItemDecoration
import com.test.chart.adapter.model.ChartItemWrapper
import com.test.chart.adapter.model.FocusState
import com.test.chart.databinding.WidgetDayChartBinding
import com.test.chart.dp
import com.test.chart.px
import kotlin.math.roundToInt

class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter(context) }

    var chartData: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            refresh()
        }

    var selectListener: (ChartItem) -> Unit = { }

    init {
        setupRecycler()
        setupLabels()
    }

    private fun setupLabels() {
        with(binding) {
            val headerCellHeight = context.px(R.dimen.header_cell_height).roundToInt()
            val itemCellHeight = context.px(R.dimen.item_cell_height).roundToInt()
            val labelStartMargin = context.px(R.dimen.label_start_margin).roundToInt()
            val labelTopMargin = context.px(R.dimen.label_top_margin).roundToInt()

            val neuralActivityLp = neuralActivityLabel.layoutParams
            neuralActivityLabel.layoutParams = LayoutParams(neuralActivityLp.height, neuralActivityLp.width).apply {
                val topMargin = (headerCellHeight * 2) + labelTopMargin
                setMargins(16.dp(), topMargin, 0, 0)
            }
            val controlLabelLp = controlLabel.layoutParams
            controlLabel.layoutParams = LayoutParams(controlLabelLp.height, controlLabelLp.width).apply {
                val topMargin = (headerCellHeight * 2) + itemCellHeight + labelTopMargin
                setMargins(labelStartMargin, topMargin, 0, 0)
            }
            val resilienceLabelLp = resilienceLabel.layoutParams
            resilienceLabel.layoutParams = LayoutParams(resilienceLabelLp.height, resilienceLabelLp.width).apply {
                val topMargin = (headerCellHeight * 2) + (itemCellHeight * 2) + labelTopMargin
                setMargins(labelStartMargin, topMargin, 0, 0)
            }
        }
    }

    private fun handleFocusState(chartItem: ChartItem) {
        val inFocusItem = chartData.firstOrNull { it.focusState == FocusState.InFocus }
        chartData = when (inFocusItem?.item?.id) {
            chartItem.id -> chartData.map { wrapper -> ChartItemWrapper(wrapper.item, FocusState.Preview) }
            else -> chartData.map { wrapper ->
                if (wrapper.item.id == chartItem.id)
                    ChartItemWrapper(wrapper.item, FocusState.InFocus)
                else
                    ChartItemWrapper(wrapper.item, FocusState.OutOfFocus)
            }
        }
    }

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                reverseLayout = true
                itemAnimator = null
            }
            addItemDecoration(ChartItemDecoration(context))
        }
    }

    private fun refresh() {
        chartAdapter.submitList(chartData)
        chartAdapter.selectListener = { item ->
            handleFocusState(item)
            selectListener(item)
        }
    }

}