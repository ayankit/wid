package com.devay.wid.screens.homeScreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devay.wid.data.room.Todo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Todo,
    textColor: Color,
    onClick: (Todo) -> Unit,
    onLongClick: (Int) -> Unit
) {

    val haptics = LocalHapticFeedback.current

    val primaryColor = MaterialTheme.colorScheme.primary
    val completedColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)

    val dateFormatter = DateTimeFormatter.ofPattern("d MMM")
    val dateFormatterYear = DateTimeFormatter.ofPattern("d MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    var lineCount = 1
    val lineList = mutableListOf<Float>()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .background(MaterialTheme.colorScheme.primary.copy(0.1f))
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {

        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = true,
                    onClick = { onClick(task) },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick(task.id!!)
                    },
                )
                .drawWithContent {
                    drawContent()

                    if (task.completed) {
                        val strokeWidth = 1.5.dp.toPx()
                        val center = size.height / (lineCount)

                        for (i in 1..lineCount) {
                            val verticalCenter = i * center - center / 2 + 5f

                            drawLine(
                                color = primaryColor,
                                strokeWidth = strokeWidth,
                                start = Offset(-60f, verticalCenter),
                                end = Offset(
                                    lineList[i - 1] + 40f,
                                    verticalCenter
                                ),
                            )
                        }
                    }
                },
            color = if (task.completed) completedColor else textColor,
            onTextLayout = {
                if (task.completed) {
                    lineCount = it.lineCount
                    for (i in 0..<lineCount) {
                        lineList.add(it.getLineRight(i))
                    }
                }
            }
        )

        val dateText = if (task.dueDate?.year == LocalDate.now().year) {
            task.dueDate.format(dateFormatter) ?: ""
        } else {
            task.dueDate?.format(dateFormatterYear) ?: ""
        }
        val timeText = task.dueTime?.format(timeFormatter) ?: ""

        val dateTimeText = when(task.dueDate) {
            LocalDate.now() -> timeText.ifBlank { "" }
            LocalDate.now().plusDays(1) -> timeText.ifBlank { "" }
            null -> timeText.ifBlank { "" }
            else -> dateText + if(timeText.isNotBlank()) " · $timeText" else ""
        }

        if(dateTimeText.isNotBlank()) {
            Text(
                text = dateTimeText,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            )
        }

    }

}