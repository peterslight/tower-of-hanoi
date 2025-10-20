package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.towerofhanoi.ui.theme.Typography

@Composable
fun Rules() {
    val rules = listOf(
        "RULES:",
        "1. You may only move one disk at a time",
        "2. You cannot place a larger disk on a smaller disk",
        "3. You must place all disks into the last tower in ascending order to win",
    )

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        rules.forEach {
            Text(
                text = it,
                textAlign = TextAlign.Start,
                style = Typography.labelMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}