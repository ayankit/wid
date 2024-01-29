package com.devay.wid.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TodoDB: RoomDatabase() {
    abstract val dao: TodoDao
}