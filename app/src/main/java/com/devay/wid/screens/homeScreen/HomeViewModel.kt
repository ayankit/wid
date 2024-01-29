package com.devay.wid.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devay.wid.data.repo.TodoRepo
import com.devay.wid.data.room.Todo
import com.devay.wid.util.Route
import com.devay.wid.util.SideMenu
import com.devay.wid.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: TodoRepo
): ViewModel() {

    private val allTasks = repo.getAllWithComplete()

    private val todayTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.dayOfYear == LocalDateTime.now().dayOfYear }
            .sortedWith(compareBy<Todo> { it.completed }.thenBy { it.dueDate })
    }

    private val tomorrowTasks = allTasks.map { tasks ->
        tasks.filter { it.dueDate?.dayOfYear == LocalDateTime.now().dayOfYear + 1 }
            .sortedBy { it.completed }
    }

    private val laterTasks = allTasks.map { tasks ->
        tasks.filterNot { it in todayTasks.first() || it in tomorrowTasks.first() }
            .sortedBy { it.completed }
    }

    private val groceryTasks = allTasks.map { todos ->
        todos.filter { it.grocery }.sortedBy { it.completed }
    }

    private val _selected = MutableStateFlow(SideMenu.TODAY)
    val selected = _selected.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks = _selected.transformLatest { selected ->
        when (selected) {
            SideMenu.TODAY -> emitAll(todayTasks)
            SideMenu.TOMORROW -> emitAll(tomorrowTasks)
            SideMenu.LATER -> emitAll(laterTasks)
            SideMenu.GROCERY -> emitAll(groceryTasks)
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun updateSelected(updatedCategory: SideMenu) {
        _selected.value = updatedCategory
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

    fun navigate() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(Route.AddEditScreen))
        }
    }

}