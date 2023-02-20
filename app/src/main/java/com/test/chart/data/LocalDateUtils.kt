package com.test.chart.data

import java.time.LocalDate
import java.time.Month

data class MonthWrapper(
    val month: Month,
    val startDay: Int? = null,
    val endDay: Int? = null
)

object LocalDateUtils {

    private fun getMonthDayList(monthWrapper: MonthWrapper): List<LocalDate> {
        val daysArray = arrayListOf<LocalDate>()
        val currentYear = LocalDate.now().year
        val firstDayOfMonth = LocalDate.of(currentYear, monthWrapper.month, 1)
        val startDay = monthWrapper.startDay ?: 1
        val endDay = monthWrapper.endDay ?: firstDayOfMonth.lengthOfMonth()
        for (dayIndex in startDay..endDay) {
            val day = LocalDate.of(currentYear, monthWrapper.month, dayIndex)
            daysArray.add(day)
        }
        return daysArray
    }

    fun getMonthDayList(vararg monthWrappers: MonthWrapper): List<LocalDate> {
        val daysArray = arrayListOf<LocalDate>()
        monthWrappers.forEach { wrapper -> daysArray.addAll(getMonthDayList(wrapper)) }
        return daysArray
    }

    fun getCurrentMonthWrapper(): MonthWrapper {
        val currentDate = LocalDate.now()
        return MonthWrapper(
            month = currentDate.month,
            startDay = 1,
            endDay = currentDate.dayOfMonth
        )
    }

}