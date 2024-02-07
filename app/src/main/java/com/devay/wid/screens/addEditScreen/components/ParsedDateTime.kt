package com.devay.wid.screens.addEditScreen.components

import java.time.LocalDate
import java.time.LocalTime

data class ParsedDateTime(
    val date: LocalDate?,
    val time: LocalTime?,
    val dateRange: IntRange,
    val timeRange: IntRange
)
