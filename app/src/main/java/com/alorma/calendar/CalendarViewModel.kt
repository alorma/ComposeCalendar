package com.alorma.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.Clock
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class CalendarViewModel(
  private val clock: Clock,
  private val locale: Locale,
) : ViewModel() {

  private val _state: MutableStateFlow<MonthData?> = MutableStateFlow(null)
  val state: StateFlow<MonthData?> = _state

  suspend fun load(yearMonth: YearMonth = YearMonth.now(clock)) = withContext(Dispatchers.IO) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    val firstDayOfWeekOnMonth = firstDayOfMonth.dayOfWeek
    val lastDayOfWeekOnMonth = lastDayOfMonth.dayOfWeek

    val daysOfMonth = (1..yearMonth.lengthOfMonth()).map { dayOfMonth -> yearMonth.atDay(dayOfMonth) }

    val allDays = daysOfMonth.toMutableList()

    // Days of past month
    DayOfWeek.values().forEach { dayOfWeek ->
      if (dayOfWeek.value < firstDayOfWeekOnMonth.value) {
        val offset = (DayOfWeek.values().size - (DayOfWeek.values().size - dayOfWeek.value))
        allDays.add(0, yearMonth.atDay(1).minusDays(offset.toLong()))
      }
    }

    // Days of next month
    DayOfWeek.values().forEach { dayOfWeek ->
      if (dayOfWeek.value > lastDayOfWeekOnMonth.value) {
        val offset = dayOfWeek.value - 1
        allDays.add(allDays.size, lastDayOfMonth.plusDays(offset.toLong()))
      }
    }

    // Fill 6th row ?
    if (allDays.size / 7 == 5) {
      DayOfWeek.values().forEach { dayOfWeek ->
        val offset = dayOfWeek.value - 1
        allDays.add(allDays.size, lastDayOfMonth.plusWeeks(1).plusDays(offset.toLong()))
      }
    }

    val days = allDays.map {
      DayData(
        value = it.dayOfMonth.toString(),
        isCurrentMonth = it.monthValue == yearMonth.monthValue
      )
    }

    _state.value = MonthData(
      "${yearMonth.month.getDisplayName(TextStyle.FULL, locale)} / ${yearMonth.year}",
      DayOfWeek.values().toList().sortedBy { it.value }.map { it.getDisplayName(TextStyle.NARROW, locale) },
      days
    )
  }
}

data class MonthData(
  val monthName: String,
  val dayNames: List<String>,
  val days: List<DayData>,
)

data class DayData(
  val value: String,
  val isCurrentMonth: Boolean
)