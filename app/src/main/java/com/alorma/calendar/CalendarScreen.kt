package com.alorma.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@Composable
fun CalendarScreen(
  calendarViewModel: CalendarViewModel
) {
  LaunchedEffect(calendarViewModel) {
    calendarViewModel.load()
  }

  val state by calendarViewModel.state.collectAsState()

  val monthData = state

  if (monthData != null) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      Box(
        modifier = Modifier
          .heightIn(64.dp)
          .fillMaxWidth(),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = monthData.monthName,
          fontWeight = FontWeight.Bold,
        )
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        monthData.dayNames.forEach { dayName ->
          Box(
            modifier = Modifier
              .heightIn(64.dp)
              .weight(1f),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = dayName,
              fontWeight = FontWeight.Bold,
            )
          }
        }
      }
      LazyVerticalGrid(
        cells = GridCells.Fixed(7)
      ) {

        items(monthData.days) { day ->
          Box(
            modifier = Modifier.heightIn(64.dp),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = day.value,
              color = if (day.isCurrentMonth) {
                MaterialTheme.colorScheme.onBackground
              } else {
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
              }
            )
          }
        }
      }
    }
  }
}