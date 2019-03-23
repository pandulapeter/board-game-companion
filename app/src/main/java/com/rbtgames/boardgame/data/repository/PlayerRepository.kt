package com.rbtgames.boardgame.data.repository

import com.rbtgames.boardgame.data.model.Player

class PlayerRepository {

    private val players = mutableListOf<Player>()

    fun getPlayer(playerId: String) = players.find { it.id == playerId }

    fun updatePlayer(player: Player) {
        deletePlayer(player.id)
        players.add(player)
    }

    fun deletePlayer(playerId: String) {
        players.removeAll { it.id == playerId }
    }
}