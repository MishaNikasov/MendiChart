package com.test.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.chart.adapter.model.ChartItemWrapper
import com.test.chart.adapter.model.FocusState
import com.test.chart.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var list: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            binding.dayChart.chartData = value
        }

    private fun prepareList(chartItem: ChartItem) {
        val inFocusItem = list.firstOrNull { it.focusState == FocusState.InFocus }
        list = when {
            inFocusItem?.item?.id == chartItem.id -> list.map { wrapper -> ChartItemWrapper(wrapper.item, FocusState.Preview) }
            else -> list.map { wrapper ->
                if (wrapper.item.id == chartItem.id)
                    ChartItemWrapper(wrapper.item, FocusState.InFocus)
                else
                    ChartItemWrapper(wrapper.item, FocusState.OutOfFocus)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val q = mutableListOf<ChartItemWrapper>()
        repeat(30) {
            q.add(
                ChartItemWrapper(
                    item = ChartItem.DayItem(
                        id = generateId(),
                        date = Date(),
                        neuralActivity = kotlin.random.Random.nextInt(2, 20).toFloat(),
                        control = kotlin.random.Random.nextInt(2, 20).toFloat(),
                        resilience = kotlin.random.Random.nextInt(2, 20).toFloat()
                    ),
                    focusState = FocusState.Preview
                )
            )
        }

        list = q

        binding.dayChart.chartData = list
        binding.dayChart.selectListener = { prepareList(it) }
    }

}