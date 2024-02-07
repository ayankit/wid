package com.devay.wid.screens.homeScreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.devay.wid.data.room.Todo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoItemList(
    tasks: List<Todo>,
    onClick: (Todo) -> Unit,
    onLongClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val haptics = LocalHapticFeedback.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 15.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.1f))
            .padding(vertical = 30.dp)
    ) {

        items(tasks, key = { it.id!! }) { task ->

            var lineCount = 1
            val lineList = mutableListOf<Float>()
            val interactionSource = remember { MutableInteractionSource() }
            val primaryColor = MaterialTheme.colorScheme.primary
            val notCompletedColor = MaterialTheme.colorScheme.onBackground
            val completedColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)

            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (task.completed) completedColor else notCompletedColor,
                onTextLayout = {
                    if (task.completed) {
                        lineCount = it.lineCount
                        for (i in 0..<lineCount) {
                            lineList.add(it.getLineRight(i))
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .animateItemPlacement()
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
                                    end = Offset(lineList[i - 1] + 40f, verticalCenter),
                                )
                            }
                        }
                    }
            )
        }
    }
}