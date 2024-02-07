package com.devay.wid.screens.addEditScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    title: String,
    rounding: Dp,
    popBackStack: () -> Unit,
    saveClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val saveCancelSpacer by animateFloatAsState(
        targetValue = if (title.isBlank()) 0f else 0.3f,
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(rounding))
                    .clickable {
                        focusManager.clearFocus()
                        popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(15.dp)
                )
            }

            Spacer(modifier = Modifier.fillMaxWidth(saveCancelSpacer))

            AnimatedVisibility(visible = title.isNotBlank()) {
                Spacer(Modifier.width(40.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(rounding))
                        .clickable(enabled = title.isNotEmpty()) {
                            focusManager.clearFocus()
                            saveClick()
                            popBackStack()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary.copy(if(title.isEmpty()) 1f else 1f),
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }


        }

    }

}