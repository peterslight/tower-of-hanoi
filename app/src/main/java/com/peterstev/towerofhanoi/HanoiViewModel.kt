package com.peterstev.towerofhanoi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterstev.towerofhanoi.states.Disk
import com.peterstev.towerofhanoi.states.PendingMove
import com.peterstev.towerofhanoi.states.generateDisks
import kotlinx.coroutines.launch

class HanoiViewModel : ViewModel() {

    var diskCount = 3
    private val _sticks = mutableStateOf(
        listOf(
            generateDisks(diskCount),
            mutableListOf(),
            mutableListOf()
        )
    )
    val sticks: State<List<List<Disk>>> get() = _sticks
    private val _selectedStick = mutableStateOf<Int?>(null)

    val pendingMove = mutableStateOf<PendingMove?>(null)

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
                return@launch
            }
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

    fun resetError() {
        _errorMessage.value = null
    }

    fun resetGame(count: Int? = null) {
        viewModelScope.launch {
            _sticks.value = listOf(
                generateDisks(count ?: diskCount),
                mutableListOf(),
                mutableListOf()
            )
            count?.let { diskCount = it }
            _selectedStick.value = null
            _steps.intValue = 0
            _errorMessage.value = null
            _gameWon.value = false
            pendingMove.value = null
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
        if (from == to || sticksCopy[from].isEmpty()) return false

        val diskToMove = sticksCopy[from].first()
        val targetTopDisk = sticksCopy[to].firstOrNull()


        if (targetTopDisk != null && diskToMove.text > targetTopDisk.text) return false

        pendingMove.value = PendingMove(
            diskId = diskToMove.id,
            from = from,
            to = to,
            targetTowerDiskCount = sticksCopy[to].size
        )
        return true
    }

    fun commitPendingMove() {
        val move = pendingMove.value ?: return
        val sticksCopy = _sticks.value.map { it.toMutableList() }.toMutableList()

        val fromStack = sticksCopy[move.from]
        if (fromStack.isEmpty() || fromStack.first().id != move.diskId) {
            pendingMove.value = null
            return
        }

        val disk = fromStack.removeAt(0)
        sticksCopy[move.to].add(0, disk)
        _sticks.value = sticksCopy

        _steps.intValue++
        checkWin()
        pendingMove.value = null
    }
}