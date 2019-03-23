package com.rbtgames.boardgame.feature.home.games.gameList.list

import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.repository.PlayerRepository

class GameViewModel(val game: Game, playerRepository: PlayerRepository) : GameListListItem {

    override val id = game.id
    val nextPlayerName = game.playerIds.mapNotNull { playerRepository.getPlayer(it) }.minBy { it.points }?.name ?: ""
}