package com.devay.wid.screens.homeScreen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devay.wid.R
import com.devay.wid.data.room.Todo
import com.vsnappy1.component.AnimatedFadeVisibility
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoItemList(
    tasks: List<Todo>,
    trashShown: Boolean,
    onClick: (Todo) -> Unit,
    onLongClick: (Int) -> Unit,
    toggleTrash: (Todo) -> Unit,
    deleteTask: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {

    val swipeDirections = when(trashShown) {
        true -> setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
        false -> setOf(DismissDirection.StartToEnd)
    }

    val onBackground = MaterialTheme.colorScheme.onBackground
    val backgroundColor = MaterialTheme.colorScheme.primary.copy(0.1f)

    val deleteComposition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.delete_animation
        )
    )

    val restoreComposition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.restore
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 15.dp))
            .background(backgroundColor)
            .padding(vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedFadeVisibility(visible = tasks.isEmpty() || tasks.dropLast(1).isEmpty()) {
            Text(
                text = "Nothing here!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
            )
        }

        AnimatedContent(
            targetState = tasks,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentKey = { tasks.lastOrNull()?.id ?: 0 },
            label = "",
        ) {tasks ->

            LazyColumn(
                state = rememberLazyListState(),
                modifier = modifier.fillMaxSize()
            ) {

                val dummyRemovedTasks = if(trashShown) tasks else tasks.dropLast(1)

                items(dummyRemovedTasks, key = { it.id!! }) { task ->

                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToEnd) {
                                if (trashShown) deleteTask(task)
                                else toggleTrash(task)
                            }
                            if (it == DismissValue.DismissedToStart) {
                                if (trashShown) toggleTrash(task)
                            }
                            true
                        },
                        positionalThreshold = { totalDistance -> totalDistance / 2f }
                    )

                    val offset = try {
                        dismissState.requireOffset() / 500f
                    } catch (_: IllegalStateException) {
                        0f
                    }

                    val coercedOffset = offset.coerceIn(-1f, 1f)
                    val animatedTextColor by animateColorAsState(
                        targetValue = when (coercedOffset) {
                            in -0.9f..-0.1f -> Color(0xFF288052)
                            in 0.1f..0.9f -> Color(0xFFCF2D40)
                            else -> onBackground
                        }, label = ""
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                LottieAnimation(
                                    composition = deleteComposition.value,
                                    progress = { offset.coerceIn(0f, 1f) },
                                    alignment = Alignment.CenterStart,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(if (trashShown) 0.4f else 0.5f)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                if (trashShown) {
                                    LottieAnimation(
                                        composition = restoreComposition.value,
                                        progress = { offset.coerceIn(-1f, 0f).absoluteValue },
                                        modifier = Modifier
                                            .requiredHeightIn(max = 50.dp)
                                            .fillMaxWidth(0.3f)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        directions = swipeDirections,
                        dismissContent = {

                            TaskItem(
                                task = task,
                                textColor = animatedTextColor,
                                onClick = { onClick(it) },
                                onLongClick = { onLongClick(it) }
                            )

                        })

                }
            }
        }

    }

}

