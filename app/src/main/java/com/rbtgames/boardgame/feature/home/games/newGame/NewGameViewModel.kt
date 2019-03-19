package com.rbtgames.boardgame.feature.home.games.newGame

import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.mutableLiveDataOf
import com.rbtgames.boardgame.utils.sendEvent

class NewGameViewModel : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()
    val isStartGameButtonEnabled = mutableLiveDataOf(false)

    fun onBackButtonPressed() = shouldNavigateBack.sendEvent()

    fun onAddPlayerButtonPressed() = Unit

    fun onStartGameButtonPressed() = Unit
}