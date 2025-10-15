package com.peterstev.towerofhanoi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.peterstev.towerofhanoi.screens.HanoiApp
import com.peterstev.towerofhanoi.ui.theme.TowerOfHanoiTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<HanoiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TowerOfHanoiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HanoiApp(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}