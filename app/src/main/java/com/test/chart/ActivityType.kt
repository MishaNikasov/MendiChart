package com.test.chart

import android.content.Context
import androidx.core.content.ContextCompat

enum class ActivityType {
    NeuralActivity,
    Control,
    Resilience;

    val selectedColor: (Context) -> Int = { context ->
        when (this) {
            NeuralActivity -> ContextCompat.getColor(context, R.color.neural_activity_selected_color)
            Control -> ContextCompat.getColor(context, R.color.control_selected_color)
            Resilience -> ContextCompat.getColor(context, R.color.resilience_selected_color)
        }
    }

    val unselectedColor: (Context) -> Int = { context ->
        when (this) {
            NeuralActivity -> ContextCompat.getColor(context, R.color.neural_activity_unselected_color)
            Control -> ContextCompat.getColor(context, R.color.control_unselected_color)
            Resilience -> ContextCompat.getColor(context, R.color.resilience_unselected_color)
        }
    }

}