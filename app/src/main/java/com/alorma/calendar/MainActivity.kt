package com.alorma.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alorma.calendar.ui.theme.CalendarTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {

  private val calendarViewModel: CalendarViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      CalendarTheme {
        CalendarScreen(calendarViewModel = calendarViewModel)
      }
    }
  }
}
