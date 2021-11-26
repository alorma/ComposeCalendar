package com.alorma.calendar.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.alorma.calendar.DayData
import com.alorma.calendar.ui.theme.CalendarTheme
import java.time.LocalDate

@Composable
fun CalendarDayItem(
  day: DayData,
  modifier: Modifier = Modifier,
  onClick: (DayData) -> Unit = {},
) {
  val color = if (day.isToday) {
    MaterialTheme.colorScheme.surface
  } else {
    MaterialTheme.colorScheme.background
  }
  val textColor = if (day.isCurrentMonth) {
    MaterialTheme.colorScheme.onBackground
  } else {
    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.60f)
  }

  Surface(
    modifier = modifier.sizeIn(minWidth = 64.dp, minHeight = 64.dp),
    color = color,
    shape = if (day.isToday) RoundedCornerShape(8.dp) else RectangleShape,
    tonalElevation = if (day.isToday) 1.dp else 0.dp,
    onClick = { onClick(day) },
  ) {
    Box(
      modifier = Modifier.sizeIn(minWidth = 64.dp, minHeight = 64.dp),
    ) {
      Text(
        modifier = Modifier.align(Alignment.Center),
        text = day.value,
        color = textColor
      )

      if (day.hasEvent) {
        Surface(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 2.dp)
            .height(4.dp)
            .fillMaxWidth(0.75f),
          shape = CircleShape,
          color = MaterialTheme.colorScheme.secondaryContainer,
        ) {}
      }
    }
  }
}

class DayProvider : CollectionPreviewParameterProvider<DayData>(
  listOf(
    DayData(
      localDate = LocalDate.now(),
      isToday = false,
      value = "23",
      isCurrentMonth = false,
      hasEvent = true,
    ),
    DayData(
      localDate = LocalDate.now(),
      isToday = true,
      value = "23",
      isCurrentMonth = true,
      hasEvent = true,
    ),
    DayData(
      localDate = LocalDate.now(),
      isToday = false,
      value = "23",
      isCurrentMonth = false,
      hasEvent = false,
    ),
    DayData(
      localDate = LocalDate.now(),
      isToday = true,
      value = "23",
      isCurrentMonth = true,
      hasEvent = false,
    )
  )
)

@Preview(
  heightDp = 80,
  widthDp = 80,
  showBackground = true,
)
@Composable
fun DayPreview(@PreviewParameter(provider = DayProvider::class) dayData: DayData) {
  CalendarTheme {
    CalendarDayItem(day = dayData)
  }
}