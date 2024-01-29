package com.devay.wid.screens.addEditScreen

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devay.wid.ui.theme.hammerSmith
import com.devay.wid.ui.theme.inraiSans
import com.devay.wid.util.TaskDate
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditScreen(
    popBackStack: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {

    val title = viewModel.title
    val selectedDate = viewModel.selectedDate

    val rounding = 20.dp

    val transitionIme = updateTransition(targetState = WindowInsets.isImeVisible, label = "")
    val transition = updateTransition(
        targetState = (WindowInsets.isImeVisible || title.isNotEmpty()),
        label = ""
    )

    val textScale by transition.animateFloat(label = "") { if (it) 0.4f else 1f }
    val boxPadding by transition.animateDp(label = "") { if (it) 25.dp else 0.dp }
    val cornerRadius by transition.animateDp(label = "") { if (it) rounding else 0.dp }

    // TODO: This height is hardcoded, needs to be fixed
    val spacerSize by transitionIme.animateDp(label = "") { if (it) 30.dp else 120.dp }

    val pxToMoveX = with(LocalDensity.current) { 100.dp.toPx().roundToInt() }
    val pxToMoveY = with(LocalDensity.current) { (-64).dp.toPx().roundToInt() }

    val offsetText by transitionIme.animateIntOffset(label = "") {
        if (it) IntOffset(pxToMoveX, pxToMoveY) else IntOffset.Zero
    }
    val offsetColumn by transitionIme.animateIntOffset(label = "") {
        if (it) IntOffset(0, pxToMoveY) else IntOffset.Zero
    }


    Scaffold(
        topBar = {
            Column {
                Text(
                    text = "Add",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(start = 25.dp, top = 40.dp)
                )
                Text(
                    text = "new task",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .offset { offsetText }
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(topStart = rounding, topEnd = rounding))
                    .background(MaterialTheme.colorScheme.primary.copy(0.7f))
                    .clickable {
                        viewModel.onAddClick()
                        popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Task",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        modifier = Modifier.safeDrawingPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset { offsetColumn }
                .padding(paddingValues)
        ) {

            Spacer(modifier = Modifier.size(spacerSize / 2))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 110.dp)
                    .offset(x = 5.dp)
                    .padding(start = 25.dp, end = boxPadding)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onBackground.copy(0.7f),
                        RoundedCornerShape(
                            topStart = rounding,
                            bottomStart = rounding,
                            topEnd = cornerRadius,
                            bottomEnd = cornerRadius
                        )
                    ),
                contentAlignment = Alignment.TopStart
            ) {

                BasicTextField(
                    value = title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = inraiSans,
                        fontWeight = FontWeight.Light,
                        fontSize = (textScale * 55).sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    maxLines = 4
                )

                if(title.isEmpty()) {
                    Text(
                        text = "Enter Task",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                            fontFamily = inraiSans,
                            fontWeight = if (textScale < 0.5) FontWeight.Light else FontWeight.Bold,
                            fontSize = (textScale * 55).sp
                        ),
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(spacerSize))

            Text(
                text = "WHEN",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 25.dp)
            )

            Spacer(Modifier.size(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(80.dp)
                    .padding(start = 25.dp)
                    .clip(RoundedCornerShape(topStart = rounding, bottomStart = rounding))
                    .background(MaterialTheme.colorScheme.primary.copy(0.05f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TaskDate.entries.forEach { taskDate ->

                    val interactionSource = remember { MutableInteractionSource() }

                    Text(
                        text = taskDate.text,
                        style = TextStyle(
                            color = if (selectedDate == taskDate)
                                MaterialTheme.colorScheme.primary.copy(0.8f) else
                                MaterialTheme.colorScheme.onBackground,
                            fontFamily = hammerSmith,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.clickable(interactionSource, null) { viewModel.onDateChange(taskDate) }
                    )
                }

            }

            Spacer(modifier = Modifier.size(spacerSize))

        }
    }

}

