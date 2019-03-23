package com.rbtgames.boardgame.feature.home.games.gameDetail

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel

class GameDetailViewModel(gameRepository: GameRepository, gameId: String) : ScreenViewModel() {

    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()

    fun onBackButtonPressed() = _shouldNavigateBack.sendEvent()
}