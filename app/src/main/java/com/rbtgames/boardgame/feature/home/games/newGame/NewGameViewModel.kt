package com.rbtgames.boardgame.feature.home.games.newGame

import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.mutableLiveDataOf
import com.rbtgames.boardgame.utils.sendEvent

class NewGameViewModel(val game: Game) : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()
    val shouldNavigateToNewPlayerScreen = eventLiveData()
    val isAddPlayerButtonEnabled = mutableLiveDataOf(true)
    val isStartGameButtonEnabled = mutableLiveDataOf(false)
    val players = mutableLiveDataOf(emptyList<Any>())

    fun refreshPlayers() {
        players.value = game.players.map { player -> PlayerViewModel(player) }.toMutableList<Any>().apply {
            if (size < MINIMUM_PLAYER_COUNT) {
                add(R.string.games_no_players)
            }
        }
        isStartGameButtonEnabled.value = game.players.size >= MINIMUM_PLAYER_COUNT
    }

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

    companion object {
        private const val MINIMUM_PLAYER_COUNT = 2
    }
}