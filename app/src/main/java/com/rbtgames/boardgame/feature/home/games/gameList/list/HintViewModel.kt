package com.rbtgames.boardgame.feature.home.games.gameList.list

import androidx.annotation.StringRes

class HintViewModel(@StringRes val hintResourceId: Int) : GameListListItem {

    override val id = hintResourceId.toString()
}