package com.devay.wid.screens.homeScreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devay.wid.R
import com.devay.wid.screens.homeScreen.components.SideMenuBar
import com.devay.wid.screens.homeScreen.components.TodoItemList
import com.devay.wid.screens.homeScreen.components.TopBar
import com.devay.wid.util.Route
import com.devay.wid.util.SideMenu
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun HomeScreen(
    onNavigate: (Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val selected = viewModel.selected.collectAsState()

    val context = LocalContext.current

    BackHandler(enabled = selected.value == SideMenu.TRASH) {
        viewModel.updateSelected(SideMenu.TODAY)
    }

    val topBarText = when (selected.value) {
        SideMenu.TODAY -> "Today"
        SideMenu.TOMORROW -> "Tomorrow"
        SideMenu.LATER -> "Later"
        SideMenu.PENDING -> "Pending"
        SideMenu.TRASH -> "Trashed"
    }

    val dateFormatter = DateTimeFormatter.ofPattern("E, d MMM")
    val dateText = when (selected.value) {
        SideMenu.TODAY -> LocalDate.now().format(dateFormatter)
        SideMenu.TOMORROW -> LocalDate.now().plusDays(1).format(dateFormatter)
        else -> ""
    }
    val totalTasksText = when (tasks.value.size - 1) {
        -1 -> ""
        0 -> "No Tasks"
        1 -> "1 Task"
        else -> "${ tasks.value.size - 1 } Tasks"
    }

    val subText = if (dateText.isNotBlank() && totalTasksText.isNotBlank()) {
        "$dateText · $totalTasksText"
    } else {
        dateText + totalTasksText
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate(Navigate(Route.AddEditScreen)) },
                elevation = FloatingActionButtonDefaults.elevation(1.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Add Button",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 20.dp, top = 20.dp),
        ) {

            TopBar(
                text = topBarText,
                subText = subText,
                trashSelected = selected.value == SideMenu.TRASH,
                onMoreClick = { Toast.makeText(context, "This won't work yet.", Toast.LENGTH_SHORT).show() },
                onSearchClick = { Toast.makeText(context, "Work in progress :)", Toast.LENGTH_SHORT).show()  },
                onTrashClick = { viewModel.updateSelected(SideMenu.TRASH) }
            )

            Spacer(Modifier.height(30.dp))

            Row {

                AnimatedVisibility(visible = selected.value != SideMenu.TRASH) {
                    SideMenuBar(selected.value, onChange = { viewModel.updateSelected(it) })
                }

                TodoItemList(
                    tasks = tasks.value,
                    trashShown = selected.value == SideMenu.TRASH,
                    onClick = { task -> viewModel.updateStatus(task) },
                    toggleTrash = { task -> viewModel.toggleTrash(task) },
                    deleteTask = { task -> viewModel.deleteTask(task) },
                    onLongClick = { onNavigate(Navigate(Route.AddEditScreen + "?taskId=$it")) }
                )
            }

        }
    }

}