package com.devay.wid.screens.addEditScreen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.devay.wid.R
import com.devay.wid.screens.addEditScreen.components.BottomBar
import com.devay.wid.screens.addEditScreen.components.CustomTextField
import com.devay.wid.screens.addEditScreen.components.DateSelector

@OptIn(ExperimentalLayoutApi::class, ExperimentalMotionApi::class)
@Composable
fun AddEditScreen(
    popBackStack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {

    val title = viewModel.title
    val editMode = viewModel.editMode
    val parsedDateTime = viewModel.parsedDateTime
    val selectedDateOption = viewModel.selectedDateOption

    val rounding = 20.dp

    val transition = updateTransition(targetState = WindowInsets.isImeVisible, label = "")

    // TODO: This height is hardcoded, needs to be fixed
    val spacerSize by transition.animateDp(label = "") { if (it) 30.dp else 120.dp }

    val motionProgress by transition.animateFloat(label = "") { if (it) 1f else 0f }

    Scaffold(
        topBar = {
            val context = LocalContext.current
            val motionScene = remember {
                context.resources.openRawResource(R.raw.text_motion).readBytes().decodeToString()
            }

            MotionLayout(
                motionScene = MotionScene(content = motionScene),
                progress = motionProgress
            ) {
                Text(
                    text = if (editMode) "Edit" else "Add",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.layoutId("text1")
                )
                Text(
                    text = if (editMode) "task" else "new task",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.layoutId("text2")
                )
            }

        },
        bottomBar = { BottomBar(
            title = title.text,
            rounding = rounding,
            popBackStack = { popBackStack() },
            saveClick = { viewModel.onAddClick() }
        ) },
        modifier = Modifier.safeDrawingPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.size(spacerSize))

            CustomTextField(
                title = title,
                dateTime = parsedDateTime,
                rounding = rounding,
                showKeyboard = false,
                removeHighlight = { viewModel.removeHighlight(it) },
                titleChange = { viewModel.onTitleChange(it) }
            )

            Spacer(modifier = Modifier.size(spacerSize))

            DateSelector(
                rounding = rounding,
                dueDate = parsedDateTime.date,
                dueTime = parsedDateTime.time,
                selectedDateOption = selectedDateOption,
                onClick = { viewModel.onDateChange(it) }
            )

        }
    }

}

