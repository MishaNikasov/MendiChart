package com.test.chart.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.chart.R
import com.test.chart.databinding.WidgetDayChartBinding
import com.test.chart.dp
import com.test.chart.px
import com.test.chart.widget.adapter.ChartAdapter
import com.test.chart.widget.adapter.ChartItemDecoration
import com.test.chart.widget.adapter.ChartSnapHelper
import com.test.chart.widget.adapter.model.ChartItem
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import kotlin.math.roundToInt

class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val TAG = "ChartWidget"

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter(context) }

    private val boundaries: Rect by lazy { Rect(left, top, right, bottom) }

    private val LONG_PRESS_DELAY = 75L
    private var tapCoordinates: Pair<Float, Float>? = null
    private var shouldIntercept: Boolean = false
    private val handler = Handler()
    private val onLongPress = Runnable {
        Log.d(TAG, "Long press")
        shouldIntercept = true
    }

    private val currentVisibleList: List<ChartItem>
        get() {
            with(binding.recycler) {
                if (layoutManager !is LinearLayoutManager?) return emptyList()
                return chartData.subList(
                    fromIndex = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                    toIndex = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                ).map { it.item }
            }
        }

    var chartData: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            chartAdapter.submitList(chartData)
        }

    private var chartCallback: ChartCallback? = null

    fun setChartCallback(chartCallback: ChartCallback) {
        this.chartCallback = chartCallback
    }

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
        chartCallback?.selectChartItem(chartItem)
    }

    private fun clearFocusStates() {
        chartData = chartData.map { wrapper -> ChartItemWrapper(wrapper.item, FocusState.Preview) }
        chartCallback?.clearSelection()
    }

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            overScrollMode = OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
                stackFromEnd = true
                itemAnimator = null
            }
            val snapHelper = ChartSnapHelper()
            snapHelper.attachToRecyclerView(this)
            addItemDecoration(ChartItemDecoration(context))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    chartCallback?.selectedChartItemList(currentVisibleList)
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun getItemAtPosition(coordinates: ItemCoordinates): ChartItemWrapper? {
        with(binding.recycler) {
            findChildViewUnder(coordinates.x, coordinates.y)?.let {
                return chartAdapter.list[getChildAdapterPosition(it)]
            } ?: return null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (boundaries.contains(left + event.x.toInt(), top + event.y.toInt())) {
                    when (val wrapper = getItemAtPosition(ItemCoordinates(event.x, event.y))) {
                        null -> clearFocusStates()
                        else -> handleFocusState(wrapper.item)
                    }
                } else { clearFocusStates() }
            }
        }
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                clearFocusStates()
                removeTouchCallback()
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
//        return false
        Log.d(TAG, "${event?.action}")
        return when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                return when {
                    tapCoordinates == null -> {
                        tapCoordinates = Pair(event.x, event.y)
                        handler.postDelayed(onLongPress, LONG_PRESS_DELAY)
                        Log.d(TAG, "tapCoordinates == null")
                        false
                    }
                    tapCoordinates?.first != event.x -> {
                        handler.removeCallbacks(onLongPress)
                        Log.d(TAG, "tapCoordinates?.first != event.x")
                        false
                    }
                    else -> {
                        Log.d(TAG, "else -> $shouldIntercept")
                        shouldIntercept
                    }
                }
            }
            else -> false
        }
    }

    private fun removeTouchCallback() {
        Log.d(TAG, "removeTouchCallback")
        handler.removeCallbacks(onLongPress)
        tapCoordinates = null
        shouldIntercept = false
    }
}