package com.rbtgames.boardgame.feature.home.games.newGame.newPlayer

import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.sendEvent

class NewPlayerViewModel : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()

    fun onCloseButtonPressed() = shouldNavigateBack.sendEvent()
}