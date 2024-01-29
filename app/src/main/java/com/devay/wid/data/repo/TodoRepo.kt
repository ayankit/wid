package com.devay.wid.data.repo

import com.devay.wid.data.room.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepo {

    suspend fun add(todo: Todo)

    suspend fun delete(todo: Todo)

    suspend fun search(text: String): List<Todo>

    suspend fun getById(id: Int): Todo?

    fun getAll(): Flow<List<Todo>>

    fun getAllWithComplete(): Flow<List<Todo>>

    fun getTrashed(): Flow<List<Todo>>

}