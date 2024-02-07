package com.devay.wid.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class Todo(
    @PrimaryKey val id: Int? = null ,
    val title: String,
    val completed: Boolean = false,
    val trashed: Boolean = false,
    val grocery: Boolean = false,
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val created: LocalDateTime = LocalDateTime.now(),
    val modified: LocalDateTime = LocalDateTime.now()
)


