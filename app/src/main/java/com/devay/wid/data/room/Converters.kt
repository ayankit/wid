package com.devay.wid.data.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateConverter {

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }

}

class TimeConverter {

    @TypeConverter
    fun fromTime(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun toTime(time: String): LocalTime {
        return LocalTime.parse(time)
    }

}

class DateTimeConverter {

    @TypeConverter
    fun fromDate(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun toDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }

}