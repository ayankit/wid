package com.devay.wid.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devay.wid.R
import com.devay.wid.screens.homeScreen.components.IconBox
import com.devay.wid.screens.homeScreen.components.SideMenuBar
import com.devay.wid.screens.homeScreen.components.TodoItemList
import com.devay.wid.screens.homeScreen.components.TopBar
import com.devay.wid.util.UiEvent


@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val selected = viewModel.selected.collectAsState()

    LaunchedEffect(true) {
        viewModel.uiEvent.collect {event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigate() },
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

            IconBox(
                onClick = { /*TODO*/ },
                painter = painterResource(id = R.drawable.more),
                modifier = Modifier.offset(x = (-10).dp)
            )

            TopBar()

            Spacer(Modifier.height(30.dp))

            Row {
                SideMenuBar(selected.value, onChange = { viewModel.updateSelected(it) })
                TodoItemList(tasks = tasks.value, onClick = { viewModel.updateStatus(it) })
            }

        }
    }

}
