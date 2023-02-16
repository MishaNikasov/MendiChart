package com.test.chart

sealed class ChartType(
    open val value: Float
) {
    data class NeuralActivity(override val value: Float): ChartType(value)
    data class Control(override val value: Float): ChartType(value)
    data class Resilience(override val value: Float): ChartType(value)
}
