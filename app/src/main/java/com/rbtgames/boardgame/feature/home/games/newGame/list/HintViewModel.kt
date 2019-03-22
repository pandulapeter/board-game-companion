package com.rbtgames.boardgame.feature.home.games.newGame.list

import androidx.annotation.StringRes

class HintViewModel(@StringRes val hintResourceId: Int) : NewGameListItem {

    override val id = hintResourceId.toString()
}