package com.rbtgames.boardgame.feature.home.games.newGame.list

import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.feature.home.games.newGame.list.NewGameListItem

class PlayerViewModel(val player: Player) : NewGameListItem {

    override val id = player.id
}