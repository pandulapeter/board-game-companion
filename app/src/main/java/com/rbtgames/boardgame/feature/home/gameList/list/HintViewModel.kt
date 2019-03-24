package com.rbtgames.boardgame.feature.home.gameList.list

import androidx.annotation.StringRes

class HintViewModel(@StringRes val hintResourceId: Int) : GameListListItem {

    override val id = "hint"
}