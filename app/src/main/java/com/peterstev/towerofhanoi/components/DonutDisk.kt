package com.peterstev.towerofhanoi.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DonutDisk(
    outerRadius: Float,
    textValue: String,
) {
    val innerRadiusRatio = 0.3f
    Box(
        modifier = Modifier.wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(
                    width = (outerRadius).dp,
                    height = (outerRadius / 2).dp
                )
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val innerRadius = outerRadius * innerRadiusRatio

            val gradient = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.3f to Color(0xFF8C5523).copy(alpha = 0.95f),
                    0.5f to Color(0xFFC9A14A).copy(alpha = 0.85f),
                    0.7f to Color(0xFFC9A14A).copy(alpha = 1.0f)
                ),
            )
            drawCircle(
                brush = gradient,
                center = center,
                radius = outerRadius,
                style = Stroke(width = outerRadius - innerRadius)
            )
        }
        Text(
            textValue,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}