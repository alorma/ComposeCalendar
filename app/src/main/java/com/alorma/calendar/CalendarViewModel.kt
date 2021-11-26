package com.alorma.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class CalendarViewModel(
  private val clock: Clock,
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

    val selectedDays = allDays.shuffleDays(10)

    val selectedDaysText = selectedDays.joinToString(separator = "\n") {
      it.toString()
    }

    Log.i("Alorma", selectedDaysText)

    val today = LocalDate.now(clock)
    val days = allDays.map { localDate: LocalDate ->
      DayData(
        value = localDate.dayOfMonth.toString(),
        isCurrentMonth = localDate.monthValue == yearMonth.monthValue,
        localDate = localDate,
        isToday = localDate.isEqual(today),
        hasEvent = localDate in selectedDays,
      )
    }

    _state.value = MonthData(
      "${yearMonth.month.getDisplayName(TextStyle.FULL, locale)} / ${yearMonth.year}",
      DayOfWeek.values().toList().sortedBy { it.value }.map { it.getDisplayName(TextStyle.NARROW, locale) },
      days
    )
  }

  private fun List<LocalDate>.shuffleDays(num: Int): Set<LocalDate> {
    val selectedDays = toMutableList()
    selectedDays.shuffle()
    return selectedDays.take(num).toSet()
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
  val localDate: LocalDate,
  val isToday: Boolean,
  val value: String,
  val isCurrentMonth: Boolean,
  val hasEvent: Boolean
)