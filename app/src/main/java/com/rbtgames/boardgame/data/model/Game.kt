package com.rbtgames.boardgame.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = Game.TABLE_NAME)
data class Game(
    @PrimaryKey @ColumnInfo(name = ID) val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "startTime") val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "lastActionTime") val lastActionTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "playerIds") val playerIds: List<String> = listOf()
) {

    companion object {
        const val TABLE_NAME = "games"
        const val ID = "id"
    }
}