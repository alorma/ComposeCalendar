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
  clock: Clock,
  private val locale: Locale,
) : ViewModel() {

  private var yearMonth: YearMonth = YearMonth.now(clock)
  private val _state: MutableStateFlow<MonthData?> = MutableStateFlow(null)
  val state: StateFlow<MonthData?> = _state

  suspend fun load() = withContext(Dispatchers.IO) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    val firstDayOfWeekOnMonth = firstDayOfMonth.dayOfWeek
    val lastDayOfWeekOnMonth = lastDayOfMonth.dayOfWeek

    val daysOfMonth = (1..yearMonth.lengthOfMonth()).map { dayOfMonth -> yearMonth.atDay(dayOfMonth) }

    val allDays = daysOfMonth.toMutableList()

    // Days of previous month
    DayOfWeek.values().forEach { dayOfWeek ->
      if (dayOfWeek.value < firstDayOfWeekOnMonth.value) {
        val offset = (DayOfWeek.values().size - (DayOfWeek.values().size - dayOfWeek.value))
        allDays.add(0, yearMonth.atDay(1).minusDays(offset.toLong()))
      }
    }

    // Days of next month
    DayOfWeek.values().forEach { dayOfWeek ->
      if (dayOfWeek.value > lastDayOfWeekOnMonth.value) {
        val offset = dayOfWeek.value - lastDayOfWeekOnMonth.value
        allDays.add(allDays.size, lastDayOfMonth.plusDays(offset.toLong()))
      }
    }

    // Fill 6th row ?
    if (allDays.size / 7 == 5) {
      DayOfWeek.values().forEach { dayOfWeek ->
        val offset = DayOfWeek.values().size - (lastDayOfWeekOnMonth.value) + dayOfWeek.value
        allDays.add(allDays.size, yearMonth.plusMonths(1).atDay(offset))
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

  suspend fun decreaseMonth() {
    yearMonth = yearMonth.minusMonths(1)
    load()
  }

  suspend fun increaseMonth() {
    yearMonth = yearMonth.plusMonths(1)
    load()
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