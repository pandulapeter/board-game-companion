package com.rbtgames.boardgame.feature.home.games.gameList.list

import com.rbtgames.boardgame.data.model.Game

class GameViewModel(val game: Game) : GameListListItem {

    override val id = game.id
    val nextPlayerName = game.players.minBy { it.points }?.name ?: ""
}