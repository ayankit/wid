package com.devay.wid.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Todo(
    @PrimaryKey val id: Int? = null ,
    val title: String,
    val completed: Boolean = false,
    val trashed: Boolean = false,
    val grocery: Boolean = false,
    val dueDate: LocalDateTime? = null,
    val created: LocalDateTime = LocalDateTime.now(),
)


