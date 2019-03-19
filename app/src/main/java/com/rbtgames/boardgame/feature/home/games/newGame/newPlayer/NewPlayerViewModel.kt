package com.rbtgames.boardgame.feature.home.games.newGame.newPlayer

import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.mutableLiveDataOf
import com.rbtgames.boardgame.utils.sendEvent

class NewPlayerViewModel : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()
    val playerName = mutableLiveDataOf("")
    val isDoneButtonEnabled = mutableLiveDataOf(false)

    init {
        playerName.observeForever { isDoneButtonEnabled.value = it.isNotEmpty() }
    }

    fun onCloseButtonPressed() = shouldNavigateBack.sendEvent()

    fun onDoneButtonPressed() {
        if (isDoneButtonEnabled.value == true) {
            shouldNavigateBack.sendEvent()
        }
    }
}