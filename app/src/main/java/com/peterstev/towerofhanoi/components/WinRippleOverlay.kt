package com.peterstev.towerofhanoi.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot

@Composable
fun WinRippleOverlay(
    color: Color = Color(0xFF4CAF50),
    text: String,
    visible: Boolean = false,
    onAnimationComplete: () -> Unit
) {
    if (!visible) return

    val scope = rememberCoroutineScope()
    val radius = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    val screenSize = LocalWindowInfo.current
    val density = LocalDensity.current

    val maxRadius = with(density) {
        hypot(
            screenSize.containerSize.width.dp.toPx(),
            screenSize.containerSize.height.dp.toPx()
        )
    }

    LaunchedEffect(Unit) {
        scope.launch {
            radius.animateTo(
                targetValue = maxRadius,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        }
        alpha.animateTo(1f, tween(400))
        delay(5000)

        scope.launch {
            radius.animateTo(
                targetValue = 0f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        }
        alpha.animateTo(0f, tween(400))
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.matchParentSize()) {
            drawCircle(
                color = color,
                radius = radius.value,
                center = Offset(size.width, size.height),
                alpha = alpha.value
            )
        }

        if (alpha.value > 0.5f) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}