package com.devay.wid.screens.addEditScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devay.wid.ui.theme.hammerSmith
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DateSelector(
    rounding: Dp,
    dueDate: LocalDate?,
    dueTime: LocalTime?,
    selectedDateOption: SelectedOption,
    onClick: (SelectedOption) -> Unit
) {


    val dateFormatter = DateTimeFormatter.ofPattern("E, d MMM")
    val dateFormatterYear = DateTimeFormatter.ofPattern("E, d MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    var formattedTime by remember { mutableStateOf("") }
    if (dueTime != null) formattedTime = dueTime.format(timeFormatter)

    var formattedDate by remember { mutableStateOf("") }
    formattedDate = if (dueDate?.year == LocalDate.now().year) {
        dueDate.format(dateFormatter)
    } else {
        dueDate?.format(dateFormatterYear) ?: ""
    }

    Text(
        text = "WHEN",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 25.dp)
    )

    Spacer(Modifier.size(10.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
            .clip(RoundedCornerShape(topStart = rounding, bottomStart = rounding))
            .background(MaterialTheme.colorScheme.primary.copy(0.05f))
            .padding(top = 25.dp, bottom = 25.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            val interactionSource = remember { MutableInteractionSource() }

            SelectedOption.entries.subList(0,3).forEach { option ->

                val text = when (option) {
                    SelectedOption.TODAY -> option.text
                    SelectedOption.TOMORROW -> option.text
                    SelectedOption.SELECT -> if (dueDate?.isAfter(LocalDate.now().plusDays(1)) == true) formattedDate else option.text
                    SelectedOption.NONE -> ""
                }

                val clickEnabled = when (option) {
                    SelectedOption.TODAY -> dueTime?.isBefore(LocalTime.now()) != true
                    SelectedOption.TOMORROW -> true
                    SelectedOption.SELECT -> !(dueDate == LocalDate.now() || dueDate == LocalDate.now().plusDays(1))
                    else -> false
                }

                val textColor: Color = if (option == selectedDateOption) MaterialTheme.colorScheme.primary.copy(0.8f)
                else if (clickEnabled) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onBackground.copy(0.5f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = text,
                        style = TextStyle(
                            color = textColor,
                            fontFamily = hammerSmith,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.clickable(interactionSource, null, clickEnabled) {
                            onClick( when (option) {
                                SelectedOption.TODAY -> if(selectedDateOption == SelectedOption.TODAY) SelectedOption.NONE else SelectedOption.TODAY
                                SelectedOption.TOMORROW -> if(selectedDateOption == SelectedOption.TOMORROW) SelectedOption.NONE else SelectedOption.TOMORROW
                                SelectedOption.SELECT -> if(selectedDateOption == SelectedOption.SELECT) SelectedOption.NONE else SelectedOption.SELECT
                                else -> SelectedOption.NONE
                            })
                        }
                    )

                    AnimatedVisibility(visible = dueTime != null && selectedDateOption == option) {
                        Text(
                            text = formattedTime,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground.copy(0.7f),
                                fontFamily = hammerSmith,
                                fontSize = 16.sp
                            )
                        )

                    }

                }

            }

        }

    }


}

enum class SelectedOption(val text: String) {
    TODAY("Today"),
    TOMORROW("Tomorrow"),
    SELECT("Select Date"),
    NONE("None")
}
