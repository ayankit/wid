package com.devay.wid.screens.addEditScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devay.wid.data.repo.TodoRepo
import com.devay.wid.data.room.Todo
import com.devay.wid.screens.addEditScreen.components.ParsedDateTime
import com.devay.wid.screens.addEditScreen.components.SelectedOption
import com.devay.wid.screens.addEditScreen.components.parseDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repo: TodoRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var task by mutableStateOf<Todo?>(null)

    var title by mutableStateOf(TextFieldValue(text = "", TextRange.Zero))
        private set

    var parsedDateTime by mutableStateOf(ParsedDateTime(LocalDate.now(), null, IntRange.EMPTY, IntRange.EMPTY))
        private set

    var editMode by mutableStateOf(false)
        private set

    var selectedDateOption by mutableStateOf(SelectedOption.TODAY)
        private set

    private var ignoreMatches = mutableStateListOf<String>()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")
        if (taskId != -1) {
            viewModelScope.launch {
                repo.getById(taskId!!)?.let { task ->
                    editMode = true
                    title = TextFieldValue(text = task.title)
                    parsedDateTime = parsedDateTime.copy(date = task.dueDate, time = task.dueTime)
                    updateDate()
                    this@AddEditViewModel.task = task
                }
            }
        }
    }

    fun onTitleChange(textField: TextFieldValue) {
        title = textField
        updateDate()
        ignoreMatches = ignoreMatches.filter { title.text.contains(it) }.toMutableStateList()
    }

    fun onDateChange(selected: SelectedOption) {
        selectedDateOption = selected

        if (selected == SelectedOption.TOMORROW) {
            title = title.copy(text = title.text.removeRange(parsedDateTime.dateRange))
        }
        if (selected == SelectedOption.TODAY && parsedDateTime.time?.isAfter(LocalTime.now()) == true) {
            title = title.copy(text = title.text.removeRange(parsedDateTime.dateRange))
        }
        if (selected == SelectedOption.NONE) {
            title = title.copy(text = if (parsedDateTime.dateRange.last > parsedDateTime.timeRange.last) {
                title.text.removeRange(parsedDateTime.dateRange).removeRange(parsedDateTime.timeRange)
            } else {
                title.text.removeRange(parsedDateTime.timeRange).removeRange(parsedDateTime.dateRange)
            })
        }

        parsedDateTime = parsedDateTime.copy(
            date = when (selected) {
                SelectedOption.TODAY -> LocalDate.now()
                SelectedOption.TOMORROW -> LocalDate.now().plusDays(1)
                else -> null
            },
            time = if (selected == SelectedOption.NONE) null else parsedDateTime.time
        )
        updateDate()
    }

    fun removeHighlight(isDate: Boolean) {
        parsedDateTime = if (isDate) {
            ignoreMatches.add(title.text.substring(parsedDateTime.dateRange))
            parsedDateTime.copy(dateRange = IntRange.EMPTY)
        } else {
            ignoreMatches.add(title.text.substring(parsedDateTime.timeRange))
            parsedDateTime.copy(timeRange = IntRange.EMPTY)
        }
        updateDate()
    }

    fun onAddClick() {
        viewModelScope.launch {
            val titleText = if (parsedDateTime.dateRange.last > parsedDateTime.timeRange.last) {
                title.text.removeRange(parsedDateTime.dateRange).removeRange(parsedDateTime.timeRange)
            } else {
                title.text.removeRange(parsedDateTime.timeRange).removeRange(parsedDateTime.dateRange)
            }
            if (titleText.isNotBlank()) {
                repo.add(
                    Todo(
                        id = task?.id,
                        title = titleText,
                        dueDate = parsedDateTime.date,
                        dueTime = parsedDateTime.time,
                        modified = LocalDateTime.now()
                    )
                )
            }
        }
    }

    private fun updateDate() {

        parsedDateTime = parseDate(title.text, parsedDateTime.date, parsedDateTime.time, ignoreMatches)

        selectedDateOption = when (parsedDateTime.date) {
            LocalDate.now() -> SelectedOption.TODAY
            LocalDate.now().plusDays(1) -> SelectedOption.TOMORROW
            null -> SelectedOption.NONE
            else -> SelectedOption.SELECT
        }
    }

}