package com.devay.wid.screens.homeScreen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devay.wid.R

@Composable
fun TopBar(
    text: String,
    subText: String,
    trashSelected: Boolean,
    onMoreClick: () -> Unit,
    onTrashClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val bgColorSelected = MaterialTheme.colorScheme.onBackground.copy(0.1f)

    val backgroundColor by animateColorAsState(
        targetValue = if (trashSelected) bgColorSelected else Color.Transparent,
        label = ""
    )

    val interactionSource = remember { MutableInteractionSource() }

    val animateProgress by animateFloatAsState(
        targetValue = if (trashSelected) 0.5f else 0f,
        animationSpec = tween(500),
        label = ""
    )

    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.hamburger_back
        )
    )

    Box(
        modifier = modifier
            .offset(x = (-10).dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
            .clickable {
                if (trashSelected) onTrashClick()
                else onMoreClick()
            },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition.value,
            progress = { animateProgress },
            modifier = Modifier.size(50.dp).scale(3f)
        )
    }


//    Box(
//        modifier = modifier
//            .offset(x = (-10).dp)
//            .clip(RoundedCornerShape(10.dp))
//            .background(Color.Transparent)
//            .clickable { onMoreClick() },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            painter = painterResource(id = R.drawable.more),
//            contentDescription = "More options button",
//            modifier = Modifier
//                .size(50.dp)
//                .padding(10.dp)
//        )
//    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(end = 20.dp)
    ) {

        Column {
            AnimatedContent(targetState = text, label = "") {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                )
            }

            AnimatedContent(targetState = subText, label = "") {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                )
            }

        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent)
                .clickable { onSearchClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search button",
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
            )
        }

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onTrashClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.trash),
                contentDescription = "Trash button",
                modifier = Modifier
                    .size(50.dp)
                    .padding(10.dp)
            )
        }

    }
}