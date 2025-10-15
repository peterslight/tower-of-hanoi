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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peterstev.towerofhanoi.HanoiViewModel
import com.peterstev.towerofhanoi.components.Rules
import com.peterstev.towerofhanoi.components.Tower
import com.peterstev.towerofhanoi.components.WinRippleOverlay

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

    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Rules()
            Spacer(Modifier.height(120.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                sticks.forEachIndexed { index, item ->
                    Tower(
                        disks = item,
                        onTap = { viewModel.onStickTapped(index) }
                    )
                }
            }

            Text(
                modifier = Modifier.padding(24.dp),
                text = "moves: $steps",
                fontSize = 24.sp
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