package com.peterstev.towerofhanoi.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.peterstev.towerofhanoi.states.Disk
import kotlin.math.abs
import kotlin.math.roundToInt

const val ANIMATION_DURATION = 450

@Composable
fun VerticalGradientStick(
    modifier: Modifier = Modifier,
    disks: List<Disk> = emptyList(),
    onTap: () -> Unit,
    originStickIndex: Int,
    pendingFrom: Int?,
    pendingTo: Int?,
    targetTowerDiskCount: Int?,
    travelUnitPx: Float,
    onMoveAnimationDone: () -> Unit,
) {
    var stickSize by remember { mutableStateOf(Size.Zero) }
    val interactionSource = remember { MutableInteractionSource() }

    val horizontalOffset = remember { Animatable(0f) }
    val verticalOffset = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    fun getSlotTopY(
        totalDisks: Int,
        indexFromTop: Int,
        stickHeight: Float,
        diskHeight: Float
    ): Float {
        val fraction = (indexFromTop + 1f) / (totalDisks + 1f)
        val centerY = stickHeight * fraction
        return centerY - diskHeight / 2f
    }

    val diskHeightPx = stickSize.width / 2f
    val isSourceStick = pendingFrom != null && pendingFrom == originStickIndex && pendingTo != null
    val topDisk = disks.firstOrNull()

    LaunchedEffect(
        pendingFrom,
        pendingTo,
        targetTowerDiskCount,
        originStickIndex,
        topDisk?.id,
        stickSize
    ) {
        val fromStick = pendingFrom
        val toStick = pendingTo
        val targetDiskCount = targetTowerDiskCount

        if (
            fromStick != null && toStick != null &&
            isSourceStick && topDisk != null &&
            stickSize.height > 0f && targetDiskCount != null
        ) {
            isAnimating = true

            val stickHeight = stickSize.height
            val sourceDiskCount = disks.size

            val sourceY = getSlotTopY(sourceDiskCount, 0, stickHeight, diskHeightPx)
            val targetY = getSlotTopY(targetDiskCount + 1, 0, stickHeight, diskHeightPx)

            val direction = if (toStick > fromStick) 1f else -1f
            val stepCount = abs(toStick - fromStick).toFloat()
            val horizontalDistance = direction * travelUnitPx * stepCount
            val liftMultiplier = 1f / (sourceDiskCount + 1f)
            val liftHeight = stickHeight * liftMultiplier

            horizontalOffset.snapTo(0f)
            verticalOffset.snapTo(sourceY)

            verticalOffset.animateTo(
                sourceY - liftHeight,
                tween(ANIMATION_DURATION, easing = LinearOutSlowInEasing)
            )
            horizontalOffset.animateTo(
                horizontalDistance,
                tween(ANIMATION_DURATION, easing = LinearOutSlowInEasing)
            )
            verticalOffset.animateTo(
                targetY,
                tween(ANIMATION_DURATION, easing = LinearOutSlowInEasing)
            )

            onMoveAnimationDone()
            horizontalOffset.snapTo(0f)
            verticalOffset.snapTo(0f)
            isAnimating = false
        }
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { stickSize = it.size.toSize() }
            .background(
                shape = RoundedCornerShape(
                    topStart = 12.dp, topEnd = 12.dp,
                    bottomStart = 8.dp, bottomEnd = 8.dp
                ),
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF8C5523), Color(0xFFC9A14A))
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.Red),
                onClick = onTap
            )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            disks.forEachIndexed { index, disk ->
                val isTopDisk = index == 0
                if (isAnimating && isSourceStick && isTopDisk) {
                    Spacer(
                        Modifier
                            .width(stickSize.width.dp)
                            .height((stickSize.width / 2f).dp)
                    )
                } else {
                    DonutDisk(
                        outerRadius = stickSize.width,
                        textValue = disk.text.toString()
                    )
                }
            }
        }

        if (isAnimating && isSourceStick && topDisk != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                DonutDisk(
                    outerRadius = stickSize.width,
                    textValue = topDisk.text.toString(),
                    modifier = Modifier.offset {
                        IntOffset(
                            x = horizontalOffset.value.roundToInt(),
                            y = verticalOffset.value.roundToInt()
                        )
                    }
                )
            }
        }
    }
}