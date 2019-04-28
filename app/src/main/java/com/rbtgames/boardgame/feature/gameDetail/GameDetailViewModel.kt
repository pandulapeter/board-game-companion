package com.rbtgames.boardgame.feature.gameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.gameDetail.list.PlayerViewModel
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
    val shouldShowFinishGameConfirmation: LiveData<Boolean?> get() = _shouldShowFinishGameConfirmation
    private val _shouldShowFinishGameConfirmation = eventLiveData()
    val isGameActive: LiveData<Boolean?> get() = _isGameActive
    private val _isGameActive = MutableLiveData<Boolean>()
    private var previousGameState: Game? = null

    init {
        points.observeForever { _isNextTurnButtonEnabled.value = !it.isNullOrBlank() }
        refreshList()
    }

    fun onBackButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onMoreButtonPressed() = _shouldShowOverflowMenu.sendEvent()

    fun onNextTurnButtonPressed() {
        if (isNextTurnButtonEnabled.value == true) {
            launch {
                val game = gameRepository.getGame(gameId)!!
                val points = try {
                    points.value?.toInt() ?: 0
                } catch (_: NumberFormatException) {
                    0
                }
                previousGameState = game
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

    fun onLoadingDone() = _shouldShowLoadingIndicator.postValue(false)

    fun isUndoAvailable() = previousGameState != null

    fun onUndoButtonPressed() {
        launch {
            previousGameState?.let {
                gameRepository.updateGame(it)
                refreshList(it)
                previousGameState = null
            }
        }
    }

    //TODO
    fun onAddCounterButtonPressed() = Unit

    //TODO
    fun onEditPlayersButtonPressed() = Unit

    fun onFinishGameButtonPressed() = _shouldShowFinishGameConfirmation.sendEvent()

    fun finishGame() {
        launch {
            val newGame = gameRepository.getGame(gameId)!!.copy(isFinished = true)
            gameRepository.updateGame(newGame)
            refreshList(newGame)
        }
    }

    private fun refreshList(game: Game? = null) {
        launch {
            val currentGame = game ?: gameRepository.getGame(gameId)!!
            val players = currentGame.players.sortedBy { it.points }
            val baselineMaximum = players.last().points
            _isGameActive.postValue(!currentGame.isFinished)
            _players.postValue(players.mapIndexed { index, player ->
                PlayerViewModel(
                    order = index,
                    player = player,
                    baselineMinimum = players.first().points,
                    baselineMaximum = baselineMaximum
                )
            }.run { if (currentGame.isFinished) this else drop(1) })
            _currentPlayer.postValue(if (currentGame.isFinished) Player(color = Player.Color.COLOR_FINISHED) else players.first())
            points.postValue("")
        }
    }
}