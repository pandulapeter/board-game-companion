package com.rbtgames.boardgame.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rbtgames.boardgame.data.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM ${Game.TABLE_NAME}")
    fun getAllGames(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Query("DELETE FROM ${Game.TABLE_NAME} WHERE ${Game.ID} IN(:gameId)")
    fun deleteGame(gameId: String)
}