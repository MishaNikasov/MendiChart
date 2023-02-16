package com.test.chart.adapter

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.test.chart.ChartItem
import com.test.chart.R
import com.test.chart.byPattern
import com.test.chart.px
import kotlin.math.roundToInt

private const val DASH_GAP = 7f

class ChartItemDecoration(private val context: Context) : ItemDecoration() {

    private val mBounds = Rect()

    private val paint: Paint
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
            val chartItemWrapper = (parent.adapter as? ChartAdapter)?.getItem(index)
            parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
            val right = mBounds.right + child.translationX.roundToInt()
            val left: Int = right
            when (val item = chartItemWrapper?.item) {
                is ChartItem.DayItem -> {
                    val dateText = item.date.byPattern("EEE")
                    val textWidth = textPaint.measureText(dateText)
                    val textStartPos = left.toFloat() - (context.px(R.dimen.day_chart_cell_width) / 2) - (textWidth / 2)
                    val textTopPosition = top.toFloat() + context.px(R.dimen.header_cell_height) + (context.px(R.dimen.header_cell_height) / 2) + (context.px(R.dimen.label_text_size) / 3)
                    val textBottomPosition = bottom.toFloat() - context.px(R.dimen.footer_cell_height) - (context.px(R.dimen.label_text_size) / 2)
                    canvas.drawText(dateText, textStartPos, textTopPosition, textPaint)
                    canvas.drawText(dateText, textStartPos, textBottomPosition, textPaint)
                    if (position != 0)
                        canvas.drawLine(left.toFloat(), top.toFloat() + context.px(R.dimen.header_cell_height), right.toFloat(), bottom.toFloat() - + context.px(R.dimen.footer_cell_height), paint)
                }
                is ChartItem.MonthItem -> {
                    when {
                        position == 0 -> Unit
                        position % 4 == 0 -> {
                            val dateText = item.date.byPattern("MMM")
                            val textStartPos = left.toFloat() + context.px(R.dimen.date_label_start_margin)
                            val textTopPosition = top.toFloat() + context.px(R.dimen.header_cell_height) + (context.px(R.dimen.header_cell_height) / 2) + (context.px(R.dimen.label_text_size) / 3)
                            canvas.drawText(dateText, textStartPos, textTopPosition, textPaint)
                            canvas.drawLine(left.toFloat(), top.toFloat() + context.px(R.dimen.header_cell_height), right.toFloat(), bottom.toFloat() - + context.px(R.dimen.footer_cell_height), paint)
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