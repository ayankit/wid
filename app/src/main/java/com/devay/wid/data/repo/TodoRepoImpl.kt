package com.devay.wid.data.repo

import com.devay.wid.data.room.Todo
import com.devay.wid.data.room.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class TodoRepoImpl(
    private val dao: TodoDao
): TodoRepo {
    override suspend fun add(todo: Todo) {
        dao.add(todo)
    }

    override suspend fun delete(todo: Todo) {
        dao.delete(todo)
    }

    override suspend fun search(text: String): List<Todo> {
        return dao.search(text)
    }

    override suspend fun getById(id: Int): Todo? {
        return dao.getById(id)
    }

    override fun getAll(): Flow<List<Todo>> {
        return dao.getAll().filterNotNull()
    }

    override fun getAllWithComplete(): Flow<List<Todo>> {
        return dao.getAllWithComplete().filterNotNull()
    }

    override fun getTrashed(): Flow<List<Todo>> {
        return dao.getTrashed().filterNotNull()
    }

}