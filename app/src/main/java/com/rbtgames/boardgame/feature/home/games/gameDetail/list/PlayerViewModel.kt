package com.rbtgames.boardgame.feature.home.games.gameDetail.list

import com.rbtgames.boardgame.data.model.Player

data class PlayerViewModel(private val order: Int, val player: Player, private val baseline: Int) {

    val turnOrder = "${order + 1}"
    val points = player.points - baseline
}