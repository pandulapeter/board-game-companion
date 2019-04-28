package com.rbtgames.boardgame.data.persistence.model

import androidx.room.Relation
import java.util.UUID

data class FullGame(
    val id: String = UUID.randomUUID().toString(),
    val startTime: Long = System.currentTimeMillis(),
    val lastActionTime: Long = System.currentTimeMillis(),
    @Relation(parentColumn = GameEntity.ID, entityColumn = PlayerEntity.GAME_ID, entity = PlayerEntity::class)
    val playerEntities: List<PlayerEntity> = listOf(),
    val isFinished: Boolean
)