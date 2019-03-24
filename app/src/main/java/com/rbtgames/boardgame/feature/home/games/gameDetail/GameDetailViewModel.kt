package com.rbtgames.boardgame.feature.home.games.gameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.gameDetail.list.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameDetailViewModel(private val gameRepository: GameRepository, private val gameId: String) : ScreenViewModel() {

    val points = mutableLiveDataOf("")
    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val currentPlayer: LiveData<Player?> get() = _currentPlayer
    private val _currentPlayer = MutableLiveData<Player?>()
    val isNextTurnButtonEnabled: LiveData<Boolean> get() = _isNextTurnButtonEnabled
    private val _isNextTurnButtonEnabled = mutableLiveDataOf(false)
    val players: LiveData<List<PlayerViewModel>> get() = _players
    private val _players = mutableLiveDataOf(emptyList<PlayerViewModel>())

    init {
        points.observeForever { _isNextTurnButtonEnabled.value = !it.isNullOrBlank() }
        refreshList()
    }

    fun onBackButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onNextTurnButtonPressed() {
        launch(Dispatchers.Default) {
            val game = gameRepository.getGame(gameId)!!
            val points = points.value?.toInt() ?: 0
            val newGame = game.copy(
                lastActionTime = System.currentTimeMillis(),
                players = game.players.map { player ->
                    if (player.id == _currentPlayer.value?.id) player.copy(points = player.points + points) else player
                }
            )
            gameRepository.updateGame(newGame)
            refreshList(newGame)
        }
    }

    private fun refreshList(game: Game? = null) {
        launch(Dispatchers.Default) {
            val players = (game ?: gameRepository.getGame(gameId)!!).players.sortedBy { it.points }
            val baseline = players.first().points
            val playerViewModels = players.mapIndexed { index, player -> PlayerViewModel(index, baseline, player) }
            launch(Dispatchers.Main) {
                _players.value = playerViewModels.drop(1)
                _currentPlayer.value = players.first()
                points.value = ""
            }
        }
    }
}