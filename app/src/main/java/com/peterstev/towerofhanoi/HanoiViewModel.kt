package com.peterstev.towerofhanoi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterstev.towerofhanoi.states.Disk
import com.peterstev.towerofhanoi.states.generateDisks
import kotlinx.coroutines.launch

class HanoiViewModel : ViewModel() {

    private val _sticks = mutableStateOf(
        listOf(
            generateDisks(3),
            mutableListOf(),
            mutableListOf()
        )
    )
    val sticks: State<List<List<Disk>>> get() = _sticks
    private val _selectedStick = mutableStateOf<Int?>(null)

    private val _movingDisk = mutableStateOf<Disk?>(null)

    private val _steps = mutableIntStateOf(0)
    val steps: State<Int> = _steps

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _gameWon = mutableStateOf(false)
    val gameWon: State<Boolean> = _gameWon

    fun onStickTapped(index: Int) {
        viewModelScope.launch {
            if (_selectedStick.value == null) {
                _selectedStick.value = index
            } else {
                val from = _selectedStick.value!!
                if (from != index) {
                    val success = tryMoveDisk(from, index)
                    if (!success) {
                        _errorMessage.value = "Cannot place larger disk on top of smaller disk"
                    } else {
                        resetError()
                    }
                }
                _selectedStick.value = null
            }
        }
    }

    fun resetError() {
        _errorMessage.value = null
    }

    fun resetGame(diskCount: Int = 3) {
        viewModelScope.launch {
            _sticks.value = listOf(
                generateDisks(diskCount),
                mutableListOf(),
                mutableListOf()
            )
            _selectedStick.value = null
            _steps.intValue = 0
            _errorMessage.value = null
            _gameWon.value = false
        }
    }

    private fun checkWin() {
        val totalDisksSize = _sticks.value.sumOf { it.size }
        val lastTower = _sticks.value.last()

        if (lastTower.size != totalDisksSize) {
            _gameWon.value = false
            return
        }

        val weights = lastTower.map { it.text }
        if (weights.size != lastTower.size) {
            _gameWon.value = false
            return
        }

        val isOrderedTopToBottom = weights.zipWithNext().all { (a, b) -> a < b }

        _gameWon.value = isOrderedTopToBottom
    }

    private fun tryMoveDisk(from: Int, to: Int): Boolean {
        val sticksCopy = _sticks.value.map { it.toMutableList() }.toMutableList()
        if (from == to || sticksCopy[from].isEmpty()) {
            return false
        }

        val diskToMove = sticksCopy[from].first()
        val targetTopDisk = sticksCopy[to].firstOrNull()

        if (targetTopDisk != null && diskToMove.text > targetTopDisk.text) {
            return false
        }

        val disk = sticksCopy[from].removeAt(0)

        _movingDisk.value = disk
        viewModelScope.launch {
            sticksCopy[to].add(0, disk)
            _sticks.value = sticksCopy
            _movingDisk.value = null
            _steps.intValue++
            checkWin()
        }
        return true
    }
}