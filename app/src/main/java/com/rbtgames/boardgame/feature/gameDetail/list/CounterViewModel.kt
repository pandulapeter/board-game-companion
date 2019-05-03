package com.rbtgames.boardgame.feature.gameDetail.list

import com.rbtgames.boardgame.data.model.Counter
import com.rbtgames.boardgame.data.model.Player

data class CounterViewModel(val player: Player, val counter: Counter, private val baselineMinimum: Int, private val baselineMaximum: Int) : GameDetailListItem {

    override val id = counter.id
    val points = counter.points - baselineMinimum
    val percentage = (baselineMaximum - baselineMinimum).let { maximum -> if (maximum == 0) maximum else Math.round(points * 100f / (maximum)) }
}