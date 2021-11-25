package com.alorma.calendar

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalendarScreen(
  calendarViewModel: CalendarViewModel = viewModel()
) {

  val state by calendarViewModel.state.collectAsState()

  Text(text = state)

}