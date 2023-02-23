package com.test.chart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.chart.data.LocalDateUtils
import com.test.chart.data.LocalDateUtils.getCurrentMonthWrapper
import com.test.chart.data.MonthWrapper
import com.test.chart.widget.adapter.model.ChartItemWrapper
import com.test.chart.widget.adapter.model.FocusState
import com.test.chart.databinding.ActivityMainBinding
import com.test.chart.widget.ChartCallback
import com.test.chart.widget.adapter.model.ChartItem
import java.time.Month
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ChartCallback {

    private lateinit var binding: ActivityMainBinding

    private var dayList: List<ChartItem.DayItem> = emptyList()
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
        val q = mutableListOf<ChartItem.DayItem>()
        LocalDateUtils.getMonthDayList(
            MonthWrapper(Month.FEBRUARY)
        ).forEach { day ->
            q.add(
                ChartItem.DayItem(
                    id = generateId(),
                    date = day,
                    neuralActivity = Random.nextInt(2, 20).toFloat(),
                    control = Random.nextInt(2, 20).toFloat(),
                    resilience = Random.nextInt(2, 20).toFloat()))
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
        Log.d("TAG", "$item")
        val itemText = "${item.date.byPattern("d, MMM")}"
        binding.item.text = itemText
    }

    override fun selectedChartItemList(listItem: List<ChartItem>) {
        val itemListText = "${listItem[0].date.byPattern("d, MMM")} - ${listItem[listItem.lastIndex].date.byPattern("d, MMM")}"
        binding.itemList.text = itemListText
        Log.d("TAG", "$listItem")
    }

    override fun clearSelection() {
        binding.item.text = ""
    }

}