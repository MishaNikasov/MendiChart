package com.test.chart.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.chart.R
import com.test.chart.widget.adapter.ChartAdapter
import com.test.chart.widget.adapter.ChartItemDecoration
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import com.test.chart.databinding.WidgetDayChartBinding
import com.test.chart.dp
import com.test.chart.px
import com.test.chart.widget.adapter.ChartSnapHelper
import kotlin.math.roundToInt

sealed class ChartCallback {
    data class SelectChartItem(val item: ChartItem): ChartCallback()
    data class SelectedChartItemList(val listItem: List<ChartItem>): ChartCallback()
}

private const val TAG = "ChartWidget"

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

    var selectListener: (ChartCallback) -> Unit = { }

    init {
        setBackgroundColor(ContextCompat.getColor(context, R.color.chart_background_color))
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
        val currentFocusItem = chartData.firstOrNull { it.focusState == FocusState.InFocus }
        if (currentFocusItem?.item?.id == chartItem.id) return
        chartData = chartData.map { wrapper ->
            if (wrapper.item.id == chartItem.id)
                ChartItemWrapper(wrapper.item, FocusState.InFocus)
            else
                ChartItemWrapper(wrapper.item, FocusState.OutOfFocus)
        }
    }

    private fun clearFocusStates() {
        chartData = chartData.map { wrapper -> ChartItemWrapper(wrapper.item, FocusState.Preview) }
    }

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                stackFromEnd = true
                itemAnimator = null
            }
            ChartSnapHelper().attachToRecyclerView(this)
            addItemDecoration(ChartItemDecoration(context))
        }
    }

    private fun refresh() {
        chartAdapter.submitList(chartData)
    }

    private fun handleSelection(wrapper: ChartItemWrapper) {
        handleFocusState(wrapper.item)
        selectListener(ChartCallback.SelectChartItem(wrapper.item))
    }

    private fun getItemAtPosition(coordinates: ItemCoordinates): ChartItemWrapper? {
        with(binding.recycler) {
            findChildViewUnder(coordinates.x, coordinates.y)?.let {
                return chartAdapter.list[getChildAdapterPosition(it)]
            }
            return null
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                getItemAtPosition(ItemCoordinates(event.x, event.y))?.let {
                    handleSelection(it)
                }
                Log.d("qawe", "${event.x}, ${event.y}")
                return true
            }
            MotionEvent.ACTION_UP -> { clearFocusStates() }
        }
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                return true
            }
        }
        return false
    }
}