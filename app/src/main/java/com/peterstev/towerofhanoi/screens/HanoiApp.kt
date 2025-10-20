package com.peterstev.towerofhanoi.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.towerofhanoi.HanoiViewModel
import com.peterstev.towerofhanoi.components.DifficultySelector
import com.peterstev.towerofhanoi.components.Rules
import com.peterstev.towerofhanoi.components.Tower
import com.peterstev.towerofhanoi.components.WinRippleOverlay
import com.peterstev.towerofhanoi.ui.theme.Typography

@Composable
fun HanoiApp(
    modifier: Modifier,
    viewModel: HanoiViewModel,
) {
    val sticks by viewModel.sticks
    val steps by viewModel.steps
    val context = LocalContext.current

    viewModel.errorMessage.value?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.resetError()
    }

    val move = viewModel.pendingMove.value
    val towerCenters = remember { mutableStateMapOf<Int, Float>() }

    val gapPx: Float = remember(towerCenters.toMap()) {
        if (towerCenters.size >= 2) {
            val xs = towerCenters.toSortedMap().values.toList()
            val gaps = xs.zipWithNext().map { (a, b) -> b - a }
            if (gaps.isNotEmpty()) gaps.average().toFloat() else 180f
        } else 180f
    }

    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Rules()
            Spacer(Modifier.height(30.dp))
            DifficultySelector { viewModel.resetGame(it) }
            Spacer(Modifier.height(100.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                sticks.forEachIndexed { index, item ->
                    Tower(
                        disks = item,
                        onTap = { viewModel.onStickTapped(index) },
                        index = index,
                        pendingFrom = move?.from,
                        pendingTo = move?.to,
                        targetTowerDiskCount = move?.targetTowerDiskCount,
                        onMoveAnimationDone = { viewModel.commitPendingMove() },
                        reportCenterX = { cx -> towerCenters[index] = cx },
                        singleGapPx = gapPx,
                    )
                }
            }

            Text(
                modifier = Modifier.padding(24.dp),
                text = "moves: $steps",
                style = Typography.labelMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Button(onClick = { viewModel.resetGame() }) {
                Text("Reset Game")
            }
        }

        WinRippleOverlay(
            visible = viewModel.gameWon.value,
            text = "🎉\nCongratulations!\nYou beat the game\nin ${viewModel.steps.value} moves!"
        ) {
            viewModel.resetGame()
        }
    }
}