package com.rbtgames.boardgame.feature.gameDetail.list

import com.rbtgames.boardgame.data.model.Player

data class PlayerViewModel(private val order: Int, val player: Player, private val baselineMinimum: Int, private val baselineMaximum: Int) {

    val turnOrder = "${order + 1}"
    val points = player.points - baselineMinimum
    val percentage = (baselineMaximum - baselineMinimum).let { maximum -> if (maximum == 0) maximum else Math.round(points * 100f / (maximum)) }
}