package com.test.chart.widget.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.test.chart.R
import com.test.chart.databinding.WidgetDayChartBinding
import com.test.chart.dp
import com.test.chart.px
import com.test.chart.widget.ChartCallback
import com.test.chart.widget.ChartUtils
import com.test.chart.widget.ItemCoordinates
import com.test.chart.widget.RangeSummary
import com.test.chart.widget.adapter.ChartAdapter
import com.test.chart.widget.adapter.ChartItemDecoration
import com.test.chart.widget.adapter.ChartSnapHelper
import com.test.chart.widget.adapter.model.ChartItem
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import kotlin.math.roundToInt

abstract class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val TAG = "ChartWidget"

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    protected abstract val itemsRange: Int

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter() }

    private var chartLayoutManagerFirstInit = false
    private val chartLayoutManager by lazy {
        val layoutManager = object : LinearLayoutManager(context) {
            override fun onLayoutCompleted(state: State?) {
                super.onLayoutCompleted(state)
                if (!chartLayoutManagerFirstInit) {
                    scrollHandler.postDelayed(visibleRangeChangedEvent, scrollIdleDelay)
                    chartLayoutManagerFirstInit = true
                }
            }
        }
        layoutManager.apply {
            orientation = LinearLayoutManager.HORIZONTAL
            stackFromEnd = true
        }
    }

    private val boundaries: Rect by lazy { Rect(left, top, right, bottom) }

    private var tapCoordinates: Pair<Float, Float>? = null
    private var shouldIntercept: Boolean = false
    private val longPressDelay = 75L
    private val touchHandler = Handler()
    private val onLongPressEvent = Runnable {
        Log.d(TAG, "Long press")
        shouldIntercept = true
    }

    private val scrollIdleDelay = 200L
    private val scrollHandler = Handler()
    private val visibleRangeChangedEvent = Runnable {
        Log.d(TAG, "onScroll")
        handleRangeChanging()
    }

    private var chartCallback: ChartCallback? = null

    fun setChartCallback(chartCallback: ChartCallback) {
        this.chartCallback = chartCallback
    }

    protected var chartData: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            chartAdapter.submitList(
                chartData,
                ChartUtils(context, value.subList(value.lastIndex + 1 - itemsRange, value.lastIndex + 1).map { it.item })
            )
        }

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

    private fun setupRecycler() {
        with(binding.recycler) {
            adapter = chartAdapter
            overScrollMode = OVER_SCROLL_NEVER
            layoutManager = chartLayoutManager
            itemAnimator = null
            val snapHelper = ChartSnapHelper()
            snapHelper.attachToRecyclerView(this)
            addItemDecoration(ChartItemDecoration(context))
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING -> scrollHandler.removeCallbacks(visibleRangeChangedEvent)
                        SCROLL_STATE_IDLE -> {
                            scrollHandler.removeCallbacks(visibleRangeChangedEvent)
                            scrollHandler.postDelayed(visibleRangeChangedEvent, scrollIdleDelay)
                        }
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
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

    private fun handleRangeChanging() {
        val utils = ChartUtils(context, getCurrentVisibleList())
        val summary = utils.getSummary()
        chartAdapter.chartUtils = utils

        chartCallback?.rangeSummary(summary)
        updateSummaryValues(summary)

        chartAdapter.notifyItemRangeChanged(
            chartLayoutManager.findFirstCompletelyVisibleItemPosition(),
            chartLayoutManager.findLastCompletelyVisibleItemPosition()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateSummaryValues(summary: RangeSummary) {
        with(binding) {
            neutralActivityValue.text = "+${summary.neuralActivity}%"
            controlValue.text = "${summary.control}s"
            resilienceValue.text = "${summary.resilience}p"
        }
    }

    private fun getCurrentVisibleList(): List<ChartItem> {
        if (chartData.isEmpty()) return emptyList()
        return chartData.subList(
            fromIndex = chartLayoutManager.findFirstCompletelyVisibleItemPosition(),
            toIndex = chartLayoutManager.findLastCompletelyVisibleItemPosition() + 1
        ).map { it.item }
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
                } else {
                    clearFocusStates()
                }
            }
            MotionEvent.ACTION_UP -> clearFocusStates()
        }
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> removeTouchCallback()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                return when {
                    tapCoordinates == null -> {
                        tapCoordinates = Pair(event.x, event.y)
                        touchHandler.postDelayed(onLongPressEvent, longPressDelay)
                        false
                    }
                    tapCoordinates?.first != event.x -> {
                        touchHandler.removeCallbacks(onLongPressEvent)
                        false
                    }
                    else -> shouldIntercept
                }
            }
            else -> false
        }
    }

    private fun removeTouchCallback() {
        Log.d(TAG, "removeTouchCallback")
        touchHandler.removeCallbacks(onLongPressEvent)
        tapCoordinates = null
        shouldIntercept = false
    }
}