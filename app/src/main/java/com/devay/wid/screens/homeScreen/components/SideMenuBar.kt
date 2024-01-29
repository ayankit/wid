package com.devay.wid.screens.homeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.devay.wid.util.SideMenu

@Composable
fun SideMenuBar(selected: SideMenu, onChange: (SideMenu) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        SideMenu.entries.forEach { category ->

            val boxColor: Color
            val textColor: Color

            if (selected == category) {
                boxColor = MaterialTheme.colorScheme.primary
                textColor = MaterialTheme.colorScheme.onPrimary
            } else {
                boxColor = MaterialTheme.colorScheme.background
                textColor = MaterialTheme.colorScheme.onBackground
            }


            Box(
                modifier = Modifier
                    .vertical()
                    .rotate(-90f)
                    .padding(bottom = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(boxColor)
                    .clickable { onChange(category) }
            ) {

                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    color = textColor,
                    modifier = Modifier.padding(10.dp)
                )
            }

        }
    }
}

// TODO: Understand how this works...
fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }