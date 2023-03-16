package com.test.chartlib.widget.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class ChartSnapHelper : LinearSnapHelper() {

    lateinit var orientationHelper: OrientationHelper

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        orientationHelper = OrientationHelper.createHorizontalHelper(recyclerView?.layoutManager)
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager, targetView: View
    ): IntArray {
        val out = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView)
        } else {
            out[0] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return if (layoutManager is LinearLayoutManager) {
            getStartView(layoutManager)
        } else {
            super.findSnapView(layoutManager)
        }
    }

    private fun distanceToStart(targetView: View): Int {
        return orientationHelper.getDecoratedStart(targetView) - orientationHelper.startAfterPadding
    }

    private fun getStartView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is LinearLayoutManager) {
            val firstChild = layoutManager.findFirstVisibleItemPosition()
            val isLastItem = (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1)
            if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }
            val child = layoutManager.findViewByPosition(firstChild)
            return if (orientationHelper.getDecoratedEnd(child) >= orientationHelper.getDecoratedMeasurement(child) / 2 && orientationHelper.getDecoratedEnd(child) > 0) {
                child
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    null
                } else {
                    layoutManager.findViewByPosition(firstChild + 1)
                }
            }
        }
        return super.findSnapView(layoutManager)
    }

}