package com.test.chart.data

import android.util.Log
import java.time.LocalDate
import java.time.Month

object LocalDateUtils {

    //: List<ChartItem.DayItem>
    fun getMonthDayList(month: Month): List<LocalDate> {
        val daysArray = arrayListOf<LocalDate>()
        val currentYear = LocalDate.now().year
        val firstDayOfMonth = LocalDate.of(currentYear, month, 1)
//        val lastDay = firstDay.with(TemporalAdjusters.lastDayOfMonth())
        val monthLength = firstDayOfMonth.lengthOfMonth()
        for (dayIndex in 1 .. monthLength) {
            daysArray.add(LocalDate.of(currentYear, month, dayIndex))
        }
        Log.i("getMonthDayList", "getMonthDayList: $daysArray")
        return daysArray
    }

//    fun getMonth

}