package com.devay.wid.data.room

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateConverter {

    @TypeConverter
    fun fromDate(date: LocalDateTime): String {
        return date.toString()
    }

    @TypeConverter
    fun toDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date)
    }

}