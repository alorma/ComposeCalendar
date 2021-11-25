package com.alorma.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CalendarViewModel: ViewModel() {

  val state: StateFlow<String> = MutableStateFlow("Hello")

}