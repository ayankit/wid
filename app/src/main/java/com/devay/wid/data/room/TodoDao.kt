package com.devay.wid.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Upsert
    suspend fun add(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM Todo WHERE Todo.title LIKE :text")
    suspend fun search(text: String): List<Todo>

    @Query("SELECT * FROM Todo WHERE Todo.trashed = 0 AND Todo.completed = 0")
    fun getAll(): Flow<List<Todo>>

    @Query("SELECT * FROM Todo WHERE Todo.trashed = 0")
    fun getAllWithComplete(): Flow<List<Todo>>

    @Query("SELECT * FROM Todo WHERE Todo.trashed = 1")
    fun getTrashed(): Flow<List<Todo>>

}