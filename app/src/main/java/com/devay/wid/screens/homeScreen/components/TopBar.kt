package com.devay.wid.screens.homeScreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devay.wid.R

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(end = 20.dp)
    ) {

        Text(
            text = "Tasks",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        IconBox(onClick = {  }, painter = painterResource(id = R.drawable.search))
        IconBox(onClick = {  }, painter = painterResource(id = R.drawable.trash))

    }
}