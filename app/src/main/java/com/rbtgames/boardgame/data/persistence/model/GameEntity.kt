package com.rbtgames.boardgame.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = GameEntity.TABLE_NAME)
data class GameEntity(
    @PrimaryKey @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = "startTime") val startTime: Long,
    @ColumnInfo(name = "lastActionTime") val lastActionTime: Long
) {

    companion object {
        const val TABLE_NAME = "games"
        const val ID = "id"
    }
}