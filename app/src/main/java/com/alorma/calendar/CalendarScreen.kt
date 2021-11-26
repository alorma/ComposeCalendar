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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alorma.calendar.components.CalendarDayItem
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@Composable
fun CalendarScreen(
  calendarViewModel: CalendarViewModel
) {
  LaunchedEffect(calendarViewModel) {
    calendarViewModel.load()
  }

  val coroutineScope = rememberCoroutineScope()

  val state by calendarViewModel.state.collectAsState()

  val monthData = state

  if (monthData != null) {
    Scaffold {
      Column(
        modifier = Modifier.fillMaxSize(),
      ) {
        MonthHeader(
          monthData = monthData,
          onPreviousClick = { coroutineScope.launch { calendarViewModel.decreaseMonth() } },
          onNextClick = { coroutineScope.launch { calendarViewModel.increaseMonth() } },
        )
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
            CalendarDayItem(day)
          }
        }
      }
    }
  }
}

@Composable
private fun MonthHeader(
  monthData: MonthData,
  onPreviousClick: () -> Unit,
  onNextClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .heightIn(64.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(onClick = onPreviousClick) {
      Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Previous month")
    }
    Text(
      modifier = Modifier.weight(1f),
      text = monthData.monthName,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
    )
    IconButton(onClick = onNextClick) {
      Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Next month")
    }
  }
}
