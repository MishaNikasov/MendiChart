package com.test.chart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.chart.data.LocalDateUtils
import com.test.chart.data.MonthWrapper
import com.test.chart.databinding.ActivityMainBinding
import com.test.chartlib.widget.utils.ChartCallback
import com.test.chartlib.widget.model.Summary
import com.test.chartlib.widget.model.ChartItem
import com.test.chartlib.widget.utils.generateId
import java.time.Month
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ChartCallback {

    private lateinit var binding: ActivityMainBinding

    private var dayList: List<ChartItem.MonthItem> = emptyList()
        set(value) {
            field = value
            binding.dayChart.dayList = value
            binding.dayChart.setChartCallback(this)
        }

//    private var monthList: List<ChartItemWrapper> = emptyList()
//        set(value) {
//            field = value
//            binding.monthChart.chartData = value
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDayChart()
    }

    private fun setupDayChart() {
        val q = mutableListOf<ChartItem.MonthItem>()
        LocalDateUtils.getMonthDayList(
            MonthWrapper(Month.JANUARY),
            MonthWrapper(Month.FEBRUARY)
        ).forEach { day ->
            q.add(
                ChartItem.MonthItem(
                    id = generateId(),
                    date = day,
                    score = Random.nextInt(10, 100),
                    neuralActivity = Random.nextInt(5, 30).toFloat(),
                    control = Random.nextInt(3, 20).toFloat(),
                    resilience = Random.nextInt(300, 2600).toFloat()
                ))
        }
        //.subList(0,4)
        dayList = q
    }

//    private fun setupMonthChart() {
//        val q = mutableListOf<ChartItemWrapper>()
//        LocalDateUtils.getMonthDayList(
//            MonthWrapper(Month.JANUARY),
//            getCurrentMonthWrapper()
//        ).forEach { day ->
//            q.add(
//                ChartItemWrapper(
//                    item = ChartItem.MonthItem(
//                        id = generateId(),
//                        date = day,
//                        neuralActivity = Random.nextInt(2, 20).toFloat(),
//                        control = Random.nextInt(2, 20).toFloat(),
//                        resilience = Random.nextInt(2, 20).toFloat()
//                    ),
//                    focusState = FocusState.Preview
//                )
//            )
//        }
//        monthList = q
//        binding.monthChart.chartData = monthList
//        binding.monthChart.setChartCallback(this)
//    }

    override fun selectChartItem(item: ChartItem) {
//        Log.d("TAG", "$item")
//        val itemText = "${item.date.byPattern("d, MMM")}"
//        binding.item.text = itemText
    }

    override fun summary(summary: Summary) {
        Log.d("TAG", "summary: $summary")
        binding.summaryWidget.data = summary
//        val itemListText = "${summary.items[0].date.byPattern("d, MMM")} - ${summary.items[summary.items.lastIndex].date.byPattern("d, MMM")}"
//        binding.itemList.text = itemListText
//        Log.d("TAG", "$summary.items")
    }

}