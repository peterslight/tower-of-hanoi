package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.peterstev.towerofhanoi.states.Disk

@Composable
fun VerticalGradientStick(
    modifier: Modifier = Modifier,
    disks: List<Disk> = emptyList(),
    onTap: () -> Unit,
) {
    var componentSize by remember { mutableStateOf(Size.Zero) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .onGloballyPositioned { componentSize = it.size.toSize() }
            .background(
                shape = RoundedCornerShape(
                    topEnd = 12.dp,
                    topStart = 12.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 8.dp,
                ),
                brush = Brush
                    .verticalGradient(
                        colors = listOf(
                            Color(0xFF8C5523),
                            Color(0xFFC9A14A),
                        )
                    )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    color = Color.Red
                ),
                onClick = onTap
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        disks.forEach { disk ->
            DonutDisk(
                outerRadius = componentSize.width,
                textValue = disk.text.toString(),
            )
        }
    }
}