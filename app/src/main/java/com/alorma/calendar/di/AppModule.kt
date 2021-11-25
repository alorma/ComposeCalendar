package com.alorma.calendar.di

import com.alorma.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

object AppModule {
  operator fun invoke() = module {
    factory(named("System")) { Locale.getDefault() }
    factory(named("System")) { Clock.systemDefaultZone() }
    factory(named("fixed")) { Clock.fixed(
      Instant.parse("2022-03-25T16:38:24.419Z"),
      ZoneId.systemDefault()
    ) }

    viewModel { CalendarViewModel(get(named("fixed")), get(named("System"))) }
  }
}