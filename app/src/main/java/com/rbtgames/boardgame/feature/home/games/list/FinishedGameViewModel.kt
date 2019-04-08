package com.rbtgames.boardgame.feature.home.games.list

import com.rbtgames.boardgame.data.model.Game

class FinishedGameViewModel(val game: Game) : GameListListItem {

    override val id = game.id
}