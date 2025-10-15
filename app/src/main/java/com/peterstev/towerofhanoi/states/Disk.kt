package com.peterstev.towerofhanoi.states

import java.util.UUID

data class Disk(
    val text: Int,
    val id: String = UUID.randomUUID().toString(),
)

fun generateDisks(count: Int): MutableList<Disk> {
    return (1..count).map { Disk(it) }.toMutableList()
}