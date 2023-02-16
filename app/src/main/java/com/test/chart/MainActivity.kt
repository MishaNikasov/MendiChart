package com.test.chart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.chart.adapter.model.ChartItemWrapper
import com.test.chart.adapter.model.FocusState
import com.test.chart.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var dayList: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            binding.dayChart.chartData = value
        }

    private var monthList: List<ChartItemWrapper> = emptyList()
        set(value) {
            field = value
            binding.monthChart.chartData = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDayChart()
        setupMonthChart()
    }

    private fun setupDayChart() {
        val q = mutableListOf<ChartItemWrapper>()
        repeat(300) {
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
        dayList = q
        binding.dayChart.chartData = dayList
    }


    private fun setupMonthChart() {
        val q = mutableListOf<ChartItemWrapper>()
        repeat(300) {
            q.add(
                ChartItemWrapper(
                    item = ChartItem.MonthItem(
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
        monthList = q
        binding.monthChart.chartData = monthList
        binding.monthChart.selectListener = {
            Log.d("TAG", "$it")
        }
    }

}