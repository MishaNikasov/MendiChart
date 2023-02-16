package com.test.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.chart.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val list = mutableListOf<ChartItem.DayItem>()
        repeat(20) {
            list.add(
                ChartItem.DayItem(
                    id = generateId(),
                    date = Date(),
                    neuralActivity = kotlin.random.Random.nextInt(2, 20).toFloat(),
                    control = kotlin.random.Random.nextInt(2, 20).toFloat(),
                    resilience = kotlin.random.Random.nextInt(2, 20).toFloat()
                )
            )
        }

        binding.dayChart.chartData = list
    }

}