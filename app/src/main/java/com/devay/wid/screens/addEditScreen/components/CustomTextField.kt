package com.devay.wid.screens.addEditScreen.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devay.wid.ui.theme.inraiSans

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomTextField(
    title: TextFieldValue,
    dateTime: ParsedDateTime,
    rounding: Dp,
    showKeyboard: Boolean,
    titleChange: (TextFieldValue) -> Unit,
    removeHighlight: (Boolean) -> Unit
) {

    val transition = updateTransition(
        targetState = (WindowInsets.isImeVisible || title.text.isNotEmpty()),
        label = ""
    )

    val textScale by transition.animateFloat(label = "") { if (it) 0.4f else 1f }
    val boxPadding by transition.animateDp(label = "") { if (it) 25.dp else 0.dp }
    val cornerRadius by transition.animateDp(label = "") { if (it) rounding else 0.dp }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(showKeyboard) {
        if (showKeyboard) focusRequester.requestFocus()
    }

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 110.dp)
                .offset(x = 5.dp)
                .padding(start = 25.dp, end = boxPadding)
                .border(
                    2.dp, MaterialTheme.colorScheme.onBackground.copy(0.7f), RoundedCornerShape(
                        topStart = rounding,
                        bottomStart = rounding,
                        topEnd = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                ),
            contentAlignment = Alignment.TopStart
        ) {

             val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

            BasicTextField(
                value = title,
                onValueChange = { newValue ->
                    val newPos = newValue.selection.end

                    titleChange( when (newPos) {
                        in dateTime.dateRange -> newValue.copy(selection = TextRange(newValue.text.length))
                        in dateTime.timeRange -> newValue.copy(selection = TextRange(newValue.text.length))
                        else -> newValue
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .focusable(enabled = true)
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = inraiSans,
                    fontWeight = FontWeight.Light,
                    fontSize = (textScale * 55).sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                ),
                onTextLayout = { layoutResult.value = it },
                visualTransformation = ColorsTransformation(
                    dateTime.dateRange,
                    dateTime.timeRange,
                    MaterialTheme.colorScheme.primary
                ),
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource, dateTime) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    val position = it.press.pressPosition
                                    val offset = layoutResult.value?.getOffsetForPosition(position)

                                    if (offset != null) {
                                        if (offset in dateTime.dateRange) removeHighlight(true)
                                        else if (offset in dateTime.timeRange) removeHighlight(false)
                                    }

                                }
                            }
                        }
                    },

            )

            if(title.text.isEmpty()) {
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
    }

}

class ColorsTransformation(
    private val dateRange: IntRange,
    private val timeRange: IntRange,
    private val textColor: Color,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            annotatedString(text.toString(), dateRange, timeRange, textColor),
            OffsetMapping.Identity
        )
    }
}

fun annotatedString(
    text: String,
    dateRange: IntRange,
    timeRange: IntRange,
    textColor: Color,
): AnnotatedString {

    val span = SpanStyle(
        color = textColor,
        fontWeight = FontWeight.Bold
    )

    return  buildAnnotatedString {
        append(text)
        if (dateRange != IntRange.EMPTY) addStyle(span, dateRange.first, dateRange.last + 1)
        if (timeRange != IntRange.EMPTY) addStyle(span, timeRange.first, timeRange.last + 1)
    }

}