package com.rbtgames.boardgame.feature.home.games.gameList.list

import com.rbtgames.boardgame.data.model.Game

class GameViewModel(val game: Game, val nextPlayerName: String) : GameListListItem {

    override val id = game.id
}