package com.test.chartlib.widget.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

fun dpToPx(dp: Float): Float = (dp * Resources.getSystem().displayMetrics.density)
fun dpToPxInt(dp: Float): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()
fun generateId(): String = UUID.randomUUID().toString()
fun pxToDpInt(px: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics).toInt()
fun pxToDp(px: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, Resources.getSystem().displayMetrics)

val RecyclerView.ViewHolder.context: Context
    get() = this.itemView.context

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(this.context)

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.px() = (this * Resources.getSystem().getDimension(this)).toInt()
fun Int.sp() = (this * Resources.getSystem().displayMetrics.scaledDensity)

fun TemporalAccessor.byPattern(pattern: String): String = DateTimeFormatter.ofPattern(pattern, Locale.US).format(this)

fun Context.px(@DimenRes dimen: Int): Float = resources.getDimension(dimen)

fun Context.dp(@DimenRes dimen: Int): Float = px(dimen) / resources.displayMetrics.density