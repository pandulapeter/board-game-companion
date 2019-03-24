package com.rbtgames.boardgame.feature.newGame.list

import androidx.annotation.StringRes

class HintViewModel(@StringRes val hintResourceId: Int) : NewGameListItem {

    override val id = "hint"
}