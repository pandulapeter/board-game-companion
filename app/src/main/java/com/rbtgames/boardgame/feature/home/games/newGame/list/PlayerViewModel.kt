package com.rbtgames.boardgame.feature.home.games.newGame.list

import com.rbtgames.boardgame.data.model.Player

class PlayerViewModel(val player: Player, val isDragHandleVisible: Boolean) : NewGameListItem {

    override val id = player.id
}