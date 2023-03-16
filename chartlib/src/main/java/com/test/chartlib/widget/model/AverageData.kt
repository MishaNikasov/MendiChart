package com.test.chartlib.widget.model

data class AverageData(
    val neuralActivity: AverageItem,
    val control: AverageItem,
    val resilience: AverageItem
)

data class AverageItem(
    val value: Float,
    val height: Int
)