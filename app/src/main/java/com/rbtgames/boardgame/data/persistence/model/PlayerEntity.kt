package com.rbtgames.boardgame.data.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rbtgames.boardgame.data.model.Player.Color

@Entity(
    tableName = PlayerEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(entity = GameEntity::class, parentColumns = [GameEntity.ID], childColumns = [PlayerEntity.GAME_ID], onDelete = ForeignKey.CASCADE)]
)
data class PlayerEntity(
    @PrimaryKey @ColumnInfo(name = PLAYER_ID) val id: String,
    @ColumnInfo(name = GAME_ID) val gameId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Color,
    @ColumnInfo(name = "points") val points: Int,
    @ColumnInfo(name = "counters") val counters: String
) {
    companion object {
        const val TABLE_NAME = "players"
        const val PLAYER_ID = "playerId"
        const val GAME_ID = "gameId"
    }
}