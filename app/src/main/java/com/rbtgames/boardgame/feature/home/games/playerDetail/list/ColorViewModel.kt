package com.rbtgames.boardgame.feature.home.games.playerDetail.list

import com.rbtgames.boardgame.data.model.Player

data class ColorViewModel(
    val color: Player.Color,
    val isSelected: Boolean
)