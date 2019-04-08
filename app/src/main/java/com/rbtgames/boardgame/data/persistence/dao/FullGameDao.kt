package com.rbtgames.boardgame.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rbtgames.boardgame.data.persistence.model.FullGame
import com.rbtgames.boardgame.data.persistence.model.GameEntity
import com.rbtgames.boardgame.data.persistence.model.PlayerEntity

@Dao
interface FullGameDao {

    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE ${GameEntity.IS_FINISHED} = 0")
    fun getActiveGames(): List<FullGame>

    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE ${GameEntity.IS_FINISHED} = 1")
    fun getFinishedGames(): List<FullGame>

    @Transaction
    @Query("SELECT * FROM ${GameEntity.TABLE_NAME} WHERE ${GameEntity.ID} IN(:gameId) LIMIT 1")
    fun getGame(gameId: String): FullGame?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: GameEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayers(players: List<PlayerEntity>)

    @Query("DELETE FROM ${GameEntity.TABLE_NAME} WHERE ${GameEntity.ID} IN(:gameId)")
    fun deleteGame(gameId: String)
}