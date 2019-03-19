package com.rbtgames.boardgame.feature.home.games.gameList

import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.sendEvent

class GameListViewModel : ScreenViewModel() {

    val shouldOpenNewGameScreen = eventLiveData()

    fun onNewButtonPressed() = shouldOpenNewGameScreen.sendEvent()
}