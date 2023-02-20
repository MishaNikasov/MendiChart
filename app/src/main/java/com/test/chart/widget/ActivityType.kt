package com.test.chart.widget

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.test.chart.R

enum class ActivityType {
    NeuralActivity,
    Control,
    Resilience;

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