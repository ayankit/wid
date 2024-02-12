package com.devay.wid.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devay.wid.data.repo.TodoRepo
import com.devay.wid.data.room.Todo
import com.devay.wid.util.SideMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: TodoRepo
): ViewModel() {

    private val allTasks = repo.getAllWithComplete()

    private val todayTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.isEqual(LocalDate.now()) == true }
            .sortedWith(compareBy<Todo> { it.completed }.thenBy(nullsLast()) { it.dueTime })
            .plus(Todo(id = -1, title = ""))
    }

    private val tomorrowTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.isEqual(LocalDate.now().plusDays(1)) == true }
            .sortedBy { it.completed }
            .plus(Todo(id = -2, title = ""))
    }

    private val laterTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.isAfter(LocalDate.now().plusDays(1)) ?: true }
            .sortedWith(compareBy<Todo> { it.completed }.thenBy { it.dueDate }.thenBy { it.dueTime })
            .plus(Todo(id = -3, title = ""))
    }

    private val pendingTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.isBefore(LocalDate.now()) == true}
            .sortedWith(compareBy<Todo> { it.completed }.thenBy { it.dueDate }.thenBy { it.dueTime })
            .plus(Todo(id = -4, title = ""))
    }

//    private val groceryTasks = allTasks.map { todos ->
//        todos.filter { it.grocery }.sortedBy { it.completed }
//    }

    private val trashedTasks = repo.getTrashed()

    private val _selected = MutableStateFlow(SideMenu.TODAY)
    val selected = _selected.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks = _selected.transformLatest { selected ->
        when (selected) {
            SideMenu.TODAY -> emitAll(todayTasks)
            SideMenu.TOMORROW -> emitAll(tomorrowTasks)
            SideMenu.LATER -> emitAll(laterTasks)
            SideMenu.PENDING -> emitAll(pendingTasks)
            SideMenu.TRASH -> emitAll(trashedTasks)
        }
    }

    fun updateSelected(updatedCategory: SideMenu) {
        if (updatedCategory == SideMenu.TRASH && _selected.value == SideMenu.TRASH) {
            _selected.value = SideMenu.TODAY
        } else {
            _selected.value = updatedCategory
        }
    }

    fun updateStatus(todo: Todo) {
        viewModelScope.launch {
            repo.add(
                todo.copy(
                    completed = !todo.completed
                )
            )
        }
    }

    fun toggleTrash(task: Todo) {
        viewModelScope.launch {
            repo.add(
                task.copy(
                    trashed = !task.trashed
                )
            )
        }
    }

    fun deleteTask(task: Todo) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }

}

data class Navigate(val route: String)