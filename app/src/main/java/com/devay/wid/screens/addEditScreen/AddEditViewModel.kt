package com.devay.wid.screens.addEditScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devay.wid.data.repo.TodoRepo
import com.devay.wid.data.room.Todo
import com.devay.wid.util.TaskDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repo: TodoRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var title by mutableStateOf("")
        private set

    var editMode by mutableStateOf(false)
        private set

    var selectedDate by mutableStateOf(TaskDate.TODAY)
        private set

    init {

        val taskId = savedStateHandle.get<Int>("taskId")

        if (taskId != -1) {
            viewModelScope.launch {
                repo.getById(taskId!!)?.let {task ->
                    editMode = true
                    title = task.title
                }
            }
        }

    }

    fun onTitleChange(text: String) {
        title = text
    }

    fun onDateChange(date: TaskDate) {
        selectedDate = date
    }

    fun onAddClick() {
        viewModelScope.launch {
            if (title.isNotEmpty()) {

                val dueDate = when (selectedDate) {
                    TaskDate.TODAY -> LocalDateTime.now()
                    TaskDate.TOMORROW -> LocalDateTime.now().plusDays(1)
                    TaskDate.SELECT -> null
                }

                repo.add(
                    Todo(title = title, dueDate = dueDate)
                )

            }
        }
    }

}