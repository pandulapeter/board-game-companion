package com.rbtgames.boardgame.feature.home.games.newGame

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.list.HintViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.list.NewGameListItem
import com.rbtgames.boardgame.feature.home.games.newGame.list.PlayerViewModel

class NewGameViewModel(val game: Game) : ScreenViewModel() {

    val shouldShowCloseConfirmation: LiveData<Boolean?> get() = _shouldShowCloseConfirmation
    private val _shouldShowCloseConfirmation = eventLiveData()
    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val shouldNavigateToNewPlayerScreen: LiveData<Boolean?> get() = _shouldNavigateToNewPlayerScreen
    private val _shouldNavigateToNewPlayerScreen = eventLiveData()
    val isAddPlayerButtonEnabled: LiveData<Boolean> get() = _isAddPlayerButtonEnabled
    private val _isAddPlayerButtonEnabled = mutableLiveDataOf(true)
    val isStartGameButtonEnabled: LiveData<Boolean> get() = _isStartGameButtonEnabled
    private val _isStartGameButtonEnabled = mutableLiveDataOf(false)
    val players: LiveData<List<NewGameListItem>> get() = _players
    private val _players = mutableLiveDataOf(emptyList<NewGameListItem>())

    fun refreshPlayers() {
        _players.value = game.players.map { player -> PlayerViewModel(player) }.toMutableList<NewGameListItem>().apply {
            if (size < MINIMUM_PLAYER_COUNT) {
                add(HintViewModel(R.string.new_game_no_players))
            }
        }
        _isStartGameButtonEnabled.value = game.players.size >= MINIMUM_PLAYER_COUNT
    }

    fun onBackButtonPressed() {
        if (_players.value?.any { it is PlayerViewModel } != true) {
            _shouldNavigateBack.sendEvent()
        } else {
            _shouldShowCloseConfirmation.sendEvent()
        }
    }

    fun onAddPlayerButtonPressed() {
        if (_isAddPlayerButtonEnabled.value == true) {
            _shouldNavigateToNewPlayerScreen.sendEvent()
        }
    }

    fun onStartGameButtonPressed() {
        if (_isStartGameButtonEnabled.value == true) {
            //TODO
        }
    }

    companion object {
        private const val MINIMUM_PLAYER_COUNT = 2
    }
}