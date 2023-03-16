package com.test.chartlib.widget.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.test.chartlib.R

enum class ActivityType {
    NeuralActivity,
    Control,
    Resilience;

    fun convertText(value: String): String {
        return when(this) {
            NeuralActivity -> "+$value%"
            Control -> "${value}s"
            Resilience -> "${value}p"
        }
    }

    val title: (Context) -> String = { context ->
        when(this) {
            NeuralActivity -> context.getString(R.string.neural_activity)
            Control -> context.getString(R.string.control)
            Resilience -> context.getString(R.string.resilience)
        }
    }

    val textColor: (Context) -> Int = { context ->
        when(this) {
            NeuralActivity -> ContextCompat.getColor(context, R.color.neural_activity_color)
            Control -> ContextCompat.getColor(context, R.color.control_color)
            Resilience -> ContextCompat.getColor(context, R.color.resilience_color)
        }
    }

    val brightTextColor: (Context) -> Int = { context ->
        when(this) {
            NeuralActivity -> ContextCompat.getColor(context, R.color.neural_activity_color_bright)
            Control -> ContextCompat.getColor(context, R.color.control_color_bright)
            Resilience -> ContextCompat.getColor(context, R.color.resilience_color_bright)
        }
    }

    val dashColor: (Context) -> Int = { context ->
        when(this) {
            NeuralActivity -> ContextCompat.getColor(context, R.color.neural_activity_dash_color)
            Control -> ContextCompat.getColor(context, R.color.control_dash_color)
            Resilience -> ContextCompat.getColor(context, R.color.resilience_dash_color)
        }
    }

    val selectedBg: (Context) -> Drawable = { context ->
        when (this) {
            NeuralActivity -> ContextCompat.getDrawable(context, R.drawable.column_neural_activity_selected)!!
            Control -> ContextCompat.getDrawable(context, R.drawable.column_control_selected)!!
            Resilience -> ContextCompat.getDrawable(context, R.drawable.column_resilience_selected)!!
        }
    }

    val unselectedBg: (Context) -> Drawable = { context ->
        when (this) {
            NeuralActivity -> ContextCompat.getDrawable(context, R.drawable.column_neural_activity_unselected)!!
            Control -> ContextCompat.getDrawable(context, R.drawable.column_control_unselected)!!
            Resilience -> ContextCompat.getDrawable(context, R.drawable.column_resilience_unselected)!!
        }
    }

}