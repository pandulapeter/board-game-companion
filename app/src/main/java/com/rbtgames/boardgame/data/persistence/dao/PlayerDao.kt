package com.rbtgames.boardgame.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rbtgames.boardgame.data.model.Player

@Dao
interface PlayerDao {

    @Query("SELECT * FROM ${Player.TABLE_NAME} WHERE ${Player.ID} IN(:playerId) LIMIT 1")
    fun getPlayer(playerId: String): List<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: Player)

    @Query("DELETE FROM ${Player.TABLE_NAME} WHERE ${Player.ID} IN(:playerId)")
    fun deletePlayer(playerId: String)
}