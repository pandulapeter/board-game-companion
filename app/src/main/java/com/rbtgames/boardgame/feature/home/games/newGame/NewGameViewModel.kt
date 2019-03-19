package com.rbtgames.boardgame.feature.home.games.newGame

import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.mutableLiveDataOf
import com.rbtgames.boardgame.utils.sendEvent

class NewGameViewModel : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()
    val shouldNavigateToNewPlayerScreen = eventLiveData()
    val isAddPlayerButtonEnabled = mutableLiveDataOf(true)
    val isStartGameButtonEnabled = mutableLiveDataOf(false)

    fun onBackButtonPressed() = shouldNavigateBack.sendEvent()

    fun onAddPlayerButtonPressed() {
        if (isAddPlayerButtonEnabled.value == true) {
            shouldNavigateToNewPlayerScreen.sendEvent()
        }
    }

    fun onStartGameButtonPressed() {
        if (isStartGameButtonEnabled.value == true) {
            //TODO
        }
    }
}