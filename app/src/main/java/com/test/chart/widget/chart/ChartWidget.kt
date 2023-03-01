package com.test.chart.widget.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.test.chart.R
import com.test.chart.databinding.WidgetDayChartBinding
import com.test.chart.widget.adapter.ChartAdapter
import com.test.chart.widget.adapter.ChartItemDecoration
import com.test.chart.widget.adapter.ChartSnapHelper
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import com.test.chart.widget.model.*
import com.test.chart.widget.utils.ChartCallback
import com.test.chart.widget.utils.px
import kotlin.math.roundToInt

abstract class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val TAG = "ChartWidget"

    private val binding = WidgetDayChartBinding.inflate(LayoutInflater.from(context), this)

    protected abstract val chartType: ChartType

    private val chartAdapter: ChartAdapter by lazy { ChartAdapter(context, chartType) }

    private var chartLayoutManagerFirstInit = false
    private val chartLayoutManager by lazy {
        val layoutManager = object : LinearLayoutManager(context) {
            override fun onLayoutCompleted(state: State?) {
                super.onLayoutCompleted(state)
                if (!chartLayoutManagerFirstInit) {
                    scrollHandler.post(visibleRangeChangedEvent)
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
        Log.d(TAG, "onLongPressEvent")
        shouldIntercept = true
    }

    private val scrollIdleDelay = 200L
    private val scrollHandler = Handler()
    private val visibleRangeChangedEvent = Runnable {
        Log.d(TAG, "visibleRangeChangedEvent")
        handleRangeChanging()
    }

    private var chartCallback: ChartCallback? = null

    fun setChartCallback(chartCallback: ChartCallback) {
        this.chartCallback = chartCallback
    }

    protected var chartData: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            chartAdapter.submitList(chartData)
        }

    init {
        setupRecycler()
        setWillNotDraw(false)
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
        chartCallback?.summary(createDaySummary(chartItem))
        chartCallback?.selectChartItem(chartItem)
    }

    private fun createDaySummary(chartItem: ChartItem) = Summary.DaySummary(
        day = chartItem.date,
        neuralActivity = chartItem.neuralActivity.roundToInt(),
        control = chartItem.control.roundToInt(),
        resilience = chartItem.resilience.roundToInt(),
    )

    private fun clearFocusStates() {
        chartData = chartData.map { wrapper -> ChartItemWrapper(wrapper.item, FocusState.Preview) }
        chartAdapter.getSummary()?.let { chartCallback?.summary(it) }
    }

    private fun handleRangeChanging() {
        val from = chartLayoutManager.findFirstCompletelyVisibleItemPosition()
        val to = chartLayoutManager.findLastCompletelyVisibleItemPosition()
        with(chartAdapter) {
            setCurrentCalculationRange(from, to)
            getSummary()?.let { summary -> chartCallback?.summary(summary) }
            notifyItemRangeChanged(from, to)
        }
        invalidate()
    }

    private fun getItemAtPosition(coordinates: ItemCoordinates): ChartItemWrapper? {
        with(binding.recycler) {
            findChildViewUnder(coordinates.x, coordinates.y)?.let {
                val position = getChildAdapterPosition(it)
                if (position > chartLayoutManager.findLastCompletelyVisibleItemPosition() ||
                    position < chartLayoutManager.findFirstCompletelyVisibleItemPosition())
                    return null
                val item = chartAdapter.list[position]
                if (item.item.isPlaceholder) return null
                else return item
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

    private val DASH_GAP = 10f

    private val textPaint: TextPaint
        get() = TextPaint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.white)
            typeface = ResourcesCompat.getFont(context, R.font.dm_sans_bold)
            textSize = context.px(R.dimen.label_text_size)
        }

    private val textBackgroundPaint: Paint
        get() = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

    private val dashLinePaint: Paint
        get() = Paint().apply {
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(DASH_GAP, DASH_GAP), 0f)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        drawLabels(canvas)
        drawAverages(canvas)
        canvas.restore()
    }

    private fun drawLabels(canvas: Canvas) {
        val headerCellHeight = context.px(R.dimen.header_cell_height)
        val itemCellHeight = context.px(R.dimen.item_cell_height)
        val labelTopMargin = context.px(R.dimen.label_top_margin)
        ActivityType.values().forEach { type ->
            val top = when (type) {
                ActivityType.NeuralActivity -> (headerCellHeight * 2) + labelTopMargin
                ActivityType.Control -> (headerCellHeight * 2) + (itemCellHeight) + labelTopMargin
                ActivityType.Resilience -> (headerCellHeight * 2) + (itemCellHeight * 2) + labelTopMargin
            }
            canvas.drawLabel(
                text = type.title(context),
                top = top,
                textColor = type.textColor(context)
            )
        }
    }

    private fun drawAverages(canvas: Canvas) {
        val headerCellHeight = context.px(R.dimen.header_cell_height)
        val itemCellHeight = context.px(R.dimen.item_cell_height)
        chartAdapter.getAverages()?.let { averages ->
            ActivityType.values().forEach { type ->
                val top = when (type) {
                    ActivityType.NeuralActivity -> (headerCellHeight * 2) + (itemCellHeight) - averages.neuralActivity.height
                    ActivityType.Control -> (headerCellHeight * 2) + (itemCellHeight * 2) - averages.control.height
                    ActivityType.Resilience -> (headerCellHeight * 2) + (itemCellHeight * 3) - averages.resilience.height
                }
                val value = when (type) {
                    ActivityType.NeuralActivity -> averages.neuralActivity.value.roundToInt()
                    ActivityType.Control -> averages.control.value.roundToInt()
                    ActivityType.Resilience -> averages.resilience.value.roundToInt()
                }
                canvas.drawAverageValue(
                    text = type.convertText(value.toString()),
                    top = top,
                    bgColor = type.textColor(context),
                    dashLineColor = type.dashColor(context)
                )
            }
        }
    }

    private fun Canvas.drawAverageValue(text: String, top: Float, bgColor: Int, dashLineColor: Int) {
        //need to calculating same height for all text
        val placeholderText = "10"
        val rightMargin = context.px(R.dimen.chart_right_margin)
        val valuesHorizontalMargin = context.px(R.dimen.values_horizontal_margin)
        val textBounds = Rect()
        textPaint.getTextBounds(placeholderText, 0, placeholderText.length, textBounds)
        val textHeight = textBounds.height()
        val textWidth = textPaint.measureText(text)
        val start = width - rightMargin + valuesHorizontalMargin * 2

        drawLine(0f, top, start, top, dashLinePaint.apply { color = dashLineColor })
        drawRoundRect(getTextBackgroundRect(start, top, textHeight, textWidth), 10f, 10f, textBackgroundPaint.apply { color = bgColor })
        drawText(text, start, top + (textHeight / 2), textPaint)
    }

    private fun Canvas.drawLabel(text: String, top: Float, textColor: Int) {
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height()
        val textWidth = textPaint.measureText(text)
        val textTop = top + textHeight

        val labelStart = context.px(R.dimen.label_start_margin) + context.px(R.dimen.chart_left_margin)

        drawRoundRect(getTextBackgroundRect(labelStart, textTop, textHeight, textWidth), 10f, 10f, textBackgroundPaint.apply {
            color = ContextCompat.getColor(context, R.color.chart_background_color)
        })
        drawText(text, labelStart, textTop + (textHeight / 2), textPaint.apply { color = textColor })
    }

    private fun getTextBackgroundRect(x: Float, y: Float, textHeight: Int, textWidth: Float) =
        RectF(x - context.px(R.dimen.values_horizontal_margin), y - textHeight, x + textWidth + context.px(R.dimen.values_horizontal_margin), y + textHeight)

}