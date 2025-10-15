package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peterstev.towerofhanoi.states.Disk

@Composable
fun Tower(
    disks: List<Disk>,
    onTap: () -> Unit,
) {
    val rawDiskSize = 120
    val diskSize = rawDiskSize.dp
    val stickHeight = 250.dp
    val stickWidth = (rawDiskSize * 0.19).dp
    val offsetHeight = (-diskSize + (((rawDiskSize * 0.095)).dp)) / 2

    Box(
        modifier = Modifier
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        TiltedGear(Modifier.size(diskSize))
        VerticalGradientStick(
            modifier = Modifier
                .height(stickHeight)
                .width(stickWidth)
                .offset(y = offsetHeight),
            disks = disks,
            onTap = onTap,
        )
    }
}