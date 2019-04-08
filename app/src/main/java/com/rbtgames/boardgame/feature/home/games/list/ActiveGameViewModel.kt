package com.rbtgames.boardgame.feature.home.games.list

import com.rbtgames.boardgame.data.model.Game

class ActiveGameViewModel(val game: Game) : GameListListItem {

    override val id = game.id
    val nextPlayerName = game.players.minBy { it.points }?.name ?: ""
}