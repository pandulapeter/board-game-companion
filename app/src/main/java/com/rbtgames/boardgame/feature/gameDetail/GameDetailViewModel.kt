package com.rbtgames.boardgame.feature.gameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.gameDetail.list.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameDetailViewModel(private val gameRepository: GameRepository, private val gameId: String) : ScreenViewModel() {

    val points = mutableLiveDataOf("")
    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val shouldShowLoadingIndicator: LiveData<Boolean> get() = _shouldShowLoadingIndicator
    private val _shouldShowLoadingIndicator = mutableLiveDataOf(true)
    val shouldShowOverflowMenu: LiveData<Boolean?> get() = _shouldShowOverflowMenu
    private val _shouldShowOverflowMenu = eventLiveData()
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

    fun onMoreButtonPressed() = _shouldShowOverflowMenu.sendEvent()

    fun onNextTurnButtonPressed() {
        if (isNextTurnButtonEnabled.value == true) {
            launch(Dispatchers.Default) {
                val game = gameRepository.getGame(gameId)!!
                val points = try {
                    points.value?.toInt() ?: 0
                } catch (_: NumberFormatException) {
                    0
                }
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
    }

    private fun refreshList(game: Game? = null) {
        launch(Dispatchers.Default) {
            val players = (game ?: gameRepository.getGame(gameId)!!).players.sortedBy { it.points }
            val baselineMinimum = players.first().points
            val baselineMaximum = players.last().points
            val playerViewModels = players.mapIndexed { index, player ->
                PlayerViewModel(
                    order = index,
                    player = player,
                    baselineMinimum = baselineMinimum,
                    baselineMaximum = baselineMaximum
                )
            }
            launch(Dispatchers.Main) {
                _players.value = playerViewModels.drop(1)
                _currentPlayer.value = players.first()
                points.value = ""
                _shouldShowLoadingIndicator.value = false
            }
        }
    }
}