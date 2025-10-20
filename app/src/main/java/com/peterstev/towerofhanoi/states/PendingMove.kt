package com.peterstev.towerofhanoi.states

data class PendingMove(val diskId: String, val from: Int, val to: Int, val targetTowerDiskCount: Int)