package com.test.chart.widget.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.test.chart.R
import com.test.chart.widget.utils.byPattern
import com.test.chart.widget.utils.px
import com.test.chart.widget.model.ChartItem
import com.test.chart.widget.adapter.model.FocusState
import kotlin.math.roundToInt

private const val DASH_GAP = 7f
private val monthHighlightDay = arrayOf(1, 9, 16, 23, 30)

class ChartItemDecoration(private val context: Context) : ItemDecoration() {

    private val mBounds = Rect()

    private val linePaint: Paint
        get() = Paint().apply {
            color = ContextCompat.getColor(context, R.color.cell_stroke_color)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }

    private val dashLinePaint: Paint
        get() = Paint().apply {
            color = ContextCompat.getColor(context, R.color.cell_stroke_color)
            isAntiAlias = true
            pathEffect = DashPathEffect(floatArrayOf(DASH_GAP, DASH_GAP, DASH_GAP, DASH_GAP), 0f)
            style = Paint.Style.STROKE
        }

    private val textPaint: Paint
        get() = Paint().apply {
            color = ContextCompat.getColor(context, R.color.date_label_text_color)
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = context.px(R.dimen.label_text_size)
        }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            val position = parent.getChildLayoutPosition(child)
            val chartItemWrapper = (parent.adapter as? ChartAdapter)?.getItem(position)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + child.translationX.roundToInt()
            val left = mBounds.left + child.translationX.roundToInt()
            when (val item = chartItemWrapper?.item) {
                is ChartItem.DayItem -> {
                    val dateText = item.date.byPattern("EEE")
                    val textWidth = textPaint.measureText(dateText)
                    val textStartPos = left.toFloat() + (child.width / 2) - (textWidth / 2)
                    val textTopPosition =
                        top.toFloat() + context.px(R.dimen.header_cell_height) + (context.px(R.dimen.header_cell_height) / 2) + (context.px(R.dimen.label_text_size) / 3)
                    val textBottomPosition = bottom.toFloat() - context.px(R.dimen.footer_cell_height) - (context.px(R.dimen.label_text_size) / 2)
                    canvas.drawText(dateText, textStartPos, textTopPosition, textPaint)
                    canvas.drawText(dateText, textStartPos, textBottomPosition, textPaint)
                    if (position == 0) {
                        canvas.drawLine(left.toFloat(), top.toFloat(), left.toFloat(), bottom.toFloat(), linePaint)
                    } else {
                        canvas.drawLine(
                            left.toFloat(),
                            top.toFloat() + context.px(R.dimen.header_cell_height),
                            left.toFloat(),
                            bottom.toFloat() - +context.px(R.dimen.footer_cell_height),
                            dashLinePaint
                        )
                    }
                }
                is ChartItem.MonthItem -> {
                    if (monthHighlightDay.contains(item.date.dayOfMonth)) {
                        val dateText = item.date.byPattern("d")
                        val textStartPos = left.toFloat() + context.px(R.dimen.date_label_start_margin)
                        val textTopPosition =
                            top.toFloat() + context.px(R.dimen.header_cell_height) + (context.px(R.dimen.header_cell_height) / 2) + (context.px(R.dimen.label_text_size) / 3)
                        val textBottomPosition = bottom.toFloat() - context.px(R.dimen.footer_cell_height) - (context.px(R.dimen.label_text_size) / 2)
                        if (item.date.dayOfMonth == 1) {
                            val monthText = item.date.byPattern("MMM")
                            val topPosition = textTopPosition - context.px(R.dimen.header_cell_height)
                            val bottomPosition = textBottomPosition + context.px(R.dimen.header_cell_height)
                            canvas.drawText(monthText, textStartPos, topPosition, textPaint)
                            canvas.drawText(monthText, textStartPos, bottomPosition, textPaint)
                        }
                        if (chartItemWrapper.focusState != FocusState.InFocus) {
                            canvas.drawText(dateText, textStartPos, textTopPosition, textPaint)
                            canvas.drawText(dateText, textStartPos, textBottomPosition, textPaint)
                        }
                        if (item.date.dayOfMonth == 1) {
                            canvas.drawLine(left.toFloat(), top.toFloat(), left.toFloat(), bottom.toFloat(), linePaint)
                        } else {
                            canvas.drawLine(
                                left.toFloat(),
                                top.toFloat() + context.px(R.dimen.header_cell_height),
                                left.toFloat(),
                                bottom.toFloat() - context.px(R.dimen.footer_cell_height),
                                dashLinePaint
                            )
                        }
                    }
                }
                is ChartItem.SixMonthItem -> TODO()
                null -> Unit
            }
        }
        canvas.restore()
    }

}