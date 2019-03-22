package com.rbtgames.boardgame.feature.home.games.newGame

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
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
    val isStartGameButtonEnabled: LiveData<Boolean> get() = _isStartGameButtonEnabled
    private val _isStartGameButtonEnabled = mutableLiveDataOf(false)
    val listItems: LiveData<List<NewGameListItem>> get() = _listItems
    private val _listItems = mutableLiveDataOf(emptyList<NewGameListItem>())
    private var playerToDeleteId: String? = null
    private val players get() = _listItems.value?.filterIsInstance<PlayerViewModel>() ?: emptyList()

    fun refreshPlayers() {
        _listItems.value = game.players.filter { it.id != playerToDeleteId }.let { players ->
            players.map { player -> PlayerViewModel(player, players.size > 1) }.toMutableList<NewGameListItem>().apply {
                when (size) {
                    0 -> add(HintViewModel(R.string.new_game_no_players))
                    1 -> add(HintViewModel(R.string.new_game_one_player))
                    else -> add(HintViewModel(R.string.new_game_two_or_more_players))
                }
            }
        }
        _isStartGameButtonEnabled.value = players.size >= MINIMUM_PLAYER_COUNT
    }

    fun swapPlayers(originalPosition: Int, targetPosition: Int) {
        _listItems.value?.toMutableList()?.let { newPlayerList ->
            if (originalPosition < targetPosition) {
                for (i in originalPosition until targetPosition) {
                    Collections.swap(newPlayerList, i, i + 1)
                }
            } else {
                for (i in originalPosition downTo targetPosition + 1) {
                    Collections.swap(newPlayerList, i, i - 1)
                }
            }
            _listItems.value = newPlayerList
            val players = players.map { it.player }.toMutableList()
            playerToDeleteId?.let { playerToDeleteId ->
                val playerToDelete = game.players.find { it.id == playerToDeleteId }
                val playerToDeleteIndex = game.players.indexOf(playerToDelete)
                if (playerToDelete != null) {
                    players.add(playerToDeleteIndex, playerToDelete)
                }
            }
            gameRepository.updateNewGame(game.copy(players = players))
        }
    }

    fun canSwipeItem(position: Int) = players.size.let { size ->
        when (size) {
            0 -> false
            else -> position != RecyclerView.NO_POSITION && position < size
        }
    }

    fun canMoveItem(position: Int) = players.size.let { size ->
        when (size) {
            0, 1 -> false
            else -> position != RecyclerView.NO_POSITION && position < size
        }
    }

    fun deletePlayerTemporarily(playerId: String) {
        playerToDeleteId = playerId
        refreshPlayers()
    }

    fun cancelDeletePlayer() {
        playerToDeleteId = null
        refreshPlayers()
    }

    fun deletePlayerPermanently() {
        if (playerToDeleteId != null) {
            gameRepository.updateNewGame(game.copy(players = players.map { it.player }))
            playerToDeleteId = null
        }
    }

    fun onBackButtonPressed() {
        if (_listItems.value?.any { it is PlayerViewModel } != true) {
            gameRepository.cancelNewGame()
            _shouldNavigateBack.sendEvent()
        } else {
            _shouldShowCloseConfirmation.sendEvent()
        }
    }

    fun onAddPlayerButtonPressed() {
        _shouldNavigateToNewPlayerScreen.sendEvent()
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