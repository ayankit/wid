package com.devay.wid.screens.addEditScreen.components

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime

fun parseDate(
    text: String,
    currentDate: LocalDate?,
    currentTime: LocalTime?,
    ignoreMatches: List<String>
): ParsedDateTime {

    val now = LocalDate.now()

    var date: LocalDate? = currentDate
    var time: LocalTime? = currentTime
    var dateRange = IntRange.EMPTY
    var timeRange = IntRange.EMPTY

    val regexDate = Regex("\\b\\s(\\d{1,2})\\s?([a-z|A-Z]{3,9})\\b")
    val matchDate = regexDate.find(text)

    val regexTime = Regex("\\b\\s(\\d{1,2})(?::|\\s)?(0{0,2}|[1-9]|[1-5][0-9])?\\s?(?i)(am|pm)\\b")
    val matchTime = regexTime.find(text)

    val regexRelativeDate = Regex("\\b\\s(?i)(today|tomorrow)\\b")
    val matchRelativeDate = regexRelativeDate.find(text)

    matchRelativeDate?.let {matchResult ->
        println("zzz ${matchResult.groupValues}")
        date = when (matchResult.groupValues[1]) {
            "today" -> LocalDate.now()
            "tomorrow" -> LocalDate.now().plusDays(1)
            else -> null
        }
        dateRange = matchResult.range
    }

    matchDate?.let { matchResult ->

        if (matchResult.groupValues[0] in ignoreMatches) { return@let }

        val day = matchResult.groupValues[1].toInt()
        val month = when(matchResult.groupValues[2].lowercase()) {
            in "january" -> 1
            in "february" -> 2
            in "march" -> 3
            in "april" -> 4
            in "may" -> 5
            in "june" -> 6
            in "july" -> 7
            in "august" -> 8
            in "september" -> 9
            in "october" -> 10
            in "november" -> 11
            in "december" -> 12
            else -> 0
        }
        val year = now.year

        try {
            val rawDate = LocalDate.of(year, month, day)
            date = if(rawDate.isBefore(now)) rawDate.plusYears(1) else rawDate
            dateRange = matchResult.range
        } catch (_: DateTimeException) { }
    }

    matchTime?.let { matchResult ->

        if (matchResult.groupValues[0] in ignoreMatches) { return@let }

        val hourInt = matchResult.groupValues[1].toInt()

        val hour = when (matchResult.groupValues[3].lowercase()) {
            "am" -> if (hourInt == 12) 0 else if (hourInt < 12) hourInt else -1
            "pm" -> if (hourInt == 12) 12 else if (hourInt < 12) hourInt + 12 else -1
            else -> -1
        }

        val minuteString = matchResult.groupValues[2]
        val minute = if (minuteString.isNotBlank()) minuteString.toInt() else 0

        try {
            time = LocalTime.of(hour, minute)

            if (date == null) {
                date = if (time?.isBefore(LocalTime.now()) == true) {
                    LocalDate.now().plusDays(1)
                } else {
                    LocalDate.now()
                }
            }

            if (date == LocalDate.now()) {
                date = if (matchDate == null && matchRelativeDate == null) {
                    if (time?.isBefore(LocalTime.now()) == true) {
                        LocalDate.now().plusDays(1)
                    } else {
                        LocalDate.now()
                    }
                } else {
                    if (time?.isBefore(LocalTime.now()) == true) {
                        return ParsedDateTime(null, null, IntRange.EMPTY, IntRange.EMPTY)
                    } else {
                        LocalDate.now()
                    }
                }
            }

            timeRange = matchResult.range
        } catch (_: DateTimeException) {  }
    }

    return ParsedDateTime(date, time, dateRange, timeRange)
}