package com.rbtgames.boardgame.feature.home.games.gameDetail

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel

class GameDetailViewModel(gameRepository: GameRepository, gameId: String) : ScreenViewModel() {

    val points = mutableLiveDataOf("")
    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val nextPlayerColor: LiveData<Player.Color> get() = _nextPlayerColor
    private val _nextPlayerColor = mutableLiveDataOf(Player.Color.COLOR_1)
    val nextPlayerName: LiveData<String> get() = _nextPlayerName
    private val _nextPlayerName = mutableLiveDataOf("")
    val isNextTurnButtonEnabled: LiveData<Boolean> get() = _isNextTurnButtonEnabled
    private val _isNextTurnButtonEnabled = mutableLiveDataOf(false)

    init {
        points.observeForever { _isNextTurnButtonEnabled.value = !it.isNullOrBlank() }
    }

    fun onBackButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onNextTurnButtonPressed() {
        points.value = ""
    }
}