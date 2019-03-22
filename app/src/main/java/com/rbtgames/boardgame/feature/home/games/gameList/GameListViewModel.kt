package com.rbtgames.boardgame.feature.home.games.gameList

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.feature.ScreenViewModel

class GameListViewModel : ScreenViewModel() {

    val shouldOpenNewGameScreen: LiveData<Boolean?> get() = _shouldOpenNewGameScreen
    private val _shouldOpenNewGameScreen = eventLiveData()

    fun onNewButtonPressed() = _shouldOpenNewGameScreen.sendEvent()
}