package com.rbtgames.boardgame.data.model

import java.util.UUID

data class Game(
    val id: String = UUID.randomUUID().toString(),
    val startTime: Long = System.currentTimeMillis(),
    val lastActionTime: Long = System.currentTimeMillis(),
    val players: List<Player> = listOf()
)