package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TiltedGear(
    modifier: Modifier = Modifier
) {
    val tilt = 60f
    Canvas(
        modifier = modifier
            .background(Color.Transparent)
    ) {

        val center = Offset(size.width / 2, size.height / 2)
        val outerRadius = size.minDimension / 2.5f
        val innerRadius = outerRadius / 3.2f
        val holeRadius = outerRadius / 8f

        val tiltRad = Math.toRadians(tilt.toDouble())
        val scaleY = cos(tiltRad).toFloat()

        fun scale(offset: Offset): Offset {
            val dx = offset.x - center.x
            val dy = offset.y - center.y
            return Offset(center.x + dx, center.y + dy * scaleY)
        }

        // Draw outer disc as ellipse
        drawOval(
            color = Color.DarkGray,
            topLeft = Offset(center.x - outerRadius, center.y - outerRadius * scaleY),
            size = androidx.compose.ui.geometry.Size(outerRadius * 2, outerRadius * 2 * scaleY)
        )

        // Center bearing
        drawOval(
            color = Color(0xFFC9A14A),
            topLeft = Offset(center.x - innerRadius, center.y - innerRadius * scaleY),
            size = androidx.compose.ui.geometry.Size(innerRadius * 2, innerRadius * 2 * scaleY)
        )

        // Hollow center
        drawOval(
            color = Color.Black,
            topLeft = Offset(center.x - innerRadius / 1.5f, center.y - innerRadius / 1.5f * scaleY),
            size = androidx.compose.ui.geometry.Size(
                innerRadius / 1.5f * 2,
                innerRadius / 1.5f * 2 * scaleY
            )
        )

        // Inner holes
        val holesCount = 6
        val holesDistance = (outerRadius + innerRadius) / 2
        repeat(holesCount) { i ->
            val angle = (2 * Math.PI / holesCount * i).toFloat()
            val holeCenter = Offset(
                x = center.x + holesDistance * cos(angle),
                y = center.y + holesDistance * sin(angle)
            )
            val projected = scale(holeCenter)
            drawOval(
                color = Color.White,
                topLeft = Offset(projected.x - holeRadius, projected.y - holeRadius * scaleY),
                size = androidx.compose.ui.geometry.Size(holeRadius * 2, holeRadius * 2 * scaleY)
            )
        }
    }
}