package com.alorma.calendar.di

import com.alorma.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
  operator fun invoke() = module {
    viewModel { CalendarViewModel() }
  }
}