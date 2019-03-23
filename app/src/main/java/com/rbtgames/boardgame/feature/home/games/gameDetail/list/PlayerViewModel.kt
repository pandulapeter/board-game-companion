package com.rbtgames.boardgame.feature.home.games.gameDetail.list

import com.rbtgames.boardgame.data.model.Player

data class PlayerViewModel(private val order: Int, val player: Player) {

    val title = "${order + 1}. ${player.name} (${player.points})"
}