package com.rbtgames.boardgame.feature.home.games.newGame

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.list.HintViewModel
import com.rbtgames.boardgame.feature.home.games.newGame.list.NewGameListItem
import com.rbtgames.boardgame.feature.home.games.newGame.list.PlayerViewModel
import java.util.Collections

class NewGameViewModel(private val gameRepository: GameRepository) : ScreenViewModel() {

    val game get() = gameRepository.getNewGame()
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
        _players.value = game.players.let { players ->
            players.map { player -> PlayerViewModel(player, players.size > 1) }.toMutableList<NewGameListItem>().apply {
                when (size) {
                    0 -> add(HintViewModel(R.string.new_game_no_players))
                    1 -> add(HintViewModel(R.string.new_game_one_player))
                    else -> add(HintViewModel(R.string.new_game_two_or_more_players))
                }
            }
        }
        _isStartGameButtonEnabled.value = game.players.size >= MINIMUM_PLAYER_COUNT
    }

    fun swapSongsInPlaylist(originalPosition: Int, targetPosition: Int) {
        _players.value?.toMutableList()?.let { newPlayerList ->
            if (originalPosition < targetPosition) {
                for (i in originalPosition until targetPosition) {
                    Collections.swap(newPlayerList, i, i + 1)
                }
            } else {
                for (i in originalPosition downTo targetPosition + 1) {
                    Collections.swap(newPlayerList, i, i - 1)
                }
            }
            _players.value = newPlayerList
            gameRepository.updateNewGame(game.copy(players = newPlayerList.filterIsInstance<PlayerViewModel>().map { it.player }))
        }
    }

    fun canSwipeItem(position: Int) = _players.value?.filterIsInstance<PlayerViewModel>()?.size?.let { size ->
        when (size) {
            0 -> false
            1 -> position == 0
            else -> position < size
        }
    } ?: false

    fun canMoveItem(position: Int) = _players.value?.filterIsInstance<PlayerViewModel>()?.size?.let { size ->
        when (size) {
            0, 1 -> false
            else -> position < size
        }
    } ?: false

    fun onBackButtonPressed() {
        if (_players.value?.any { it is PlayerViewModel } != true) {
            gameRepository.cancelNewGame()
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
            gameRepository.confirmNewGame(gameRepository.getNewGame().copy(startTime = System.currentTimeMillis()))
            _shouldNavigateBack.sendEvent() //TODO: Remove this
        }
    }

    companion object {
        private const val MINIMUM_PLAYER_COUNT = 2
    }
}