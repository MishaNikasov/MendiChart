package com.test.chart.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.test.chart.R
import kotlin.math.roundToInt

private const val DASH_GAP = 7f
private const val STROKE_COLOR = R.color.cell_stroke_color

class ChartItemDecoration(private val context: Context) : ItemDecoration() {

    private val mBounds = Rect()
    private val paint: Paint
        get() {
            return Paint().apply {
                color = ContextCompat.getColor(context, STROKE_COLOR)
                pathEffect = DashPathEffect(floatArrayOf(DASH_GAP, DASH_GAP, DASH_GAP, DASH_GAP), 0f)
                style = Paint.Style.STROKE
            }
        }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
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
        for (i in 0 until childCount) {
            when(i) {
                0 -> Unit
                else -> {
                    val child = parent.getChildAt(i)
                    parent.layoutManager!!.getDecoratedBoundsWithMargins(child, mBounds)
                    val right = mBounds.right + child.translationX.roundToInt()
                    val left: Int = right
                    canvas.drawLine(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
                }
            }
        }
        canvas.restore()
    }

}