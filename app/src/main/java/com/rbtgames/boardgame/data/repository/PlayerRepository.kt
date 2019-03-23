package com.rbtgames.boardgame.data.repository

import com.rbtgames.boardgame.data.model.Player
import kotlinx.coroutines.delay

class PlayerRepository {

    private val players = mutableListOf<Player>()

    suspend fun getPlayer(playerId: String): Player? {
        delay(200)
        return players.find { it.id == playerId }
    }

    fun updatePlayer(player: Player) {
        deletePlayer(player.id)
        players.add(player)
    }

    fun deletePlayer(playerId: String) {
        players.removeAll { it.id == playerId }
    }
}