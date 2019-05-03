package com.rbtgames.boardgame.feature.gameDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rbtgames.boardgame.data.model.Counter
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.gameDetail.list.CounterViewModel
import com.rbtgames.boardgame.feature.gameDetail.list.GameDetailListItem
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
    val isDoneButtonEnabled: LiveData<Boolean> get() = _isDoneButtonEnabled
    private val _isDoneButtonEnabled = mutableLiveDataOf(false)
    val players: LiveData<List<GameDetailListItem>> get() = _players
    private val _players = mutableLiveDataOf(emptyList<GameDetailListItem>())
    val shouldShowFinishGameConfirmation: LiveData<Boolean?> get() = _shouldShowFinishGameConfirmation
    private val _shouldShowFinishGameConfirmation = eventLiveData()
    val isGameActive: LiveData<Boolean?> get() = _isGameActive
    private val _isGameActive = MutableLiveData<Boolean?>()
    val shouldShowCounterDialog: LiveData<Boolean?> get() = _shouldShowCounterDialog
    private val _shouldShowCounterDialog = eventLiveData()
    private var previousGameState: Game? = null

    init {
        points.observeForever { _isDoneButtonEnabled.value = !it.isNullOrBlank() }
        refreshList()
    }

    fun onBackButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onMoreButtonPressed() = _shouldShowOverflowMenu.sendEvent()

    fun onDoneButtonPressed() {
        if (isDoneButtonEnabled.value == true) {
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

    fun onAddCounterButtonPressed() = _shouldShowCounterDialog.sendEvent()

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

    fun addCounter(counter: Counter) {
        launch {
            val game = gameRepository.getGame(gameId)!!
            previousGameState = game
            val newGame = game.copy(
                lastActionTime = System.currentTimeMillis(),
                players = game.players.map { player ->
                    if (player.id == _currentPlayer.value?.id) player.copy(counters = player.counters.toMutableList().apply {
                        add(counter)
                    }) else player
                }
            )
            gameRepository.updateGame(newGame)
            refreshList(newGame)
        }
    }

    private fun refreshList(game: Game? = null) {
        launch {
            val currentGame = game ?: gameRepository.getGame(gameId)!!
            val players = currentGame.players.reversed().sortedBy { it.points }
            _isGameActive.postValue(!currentGame.isFinished)
            _players.postValue(players.toViewModels(currentGame))
            _currentPlayer.postValue(if (currentGame.isFinished) Player(color = Player.Color.COLOR_FINISHED) else players.first())
            points.postValue("")
        }
    }

    private fun List<Player>.toViewModels(currentGame: Game): List<GameDetailListItem> {
        val baselineMaximum = last().points
        val items = mutableListOf<GameDetailListItem>()
        forEachIndexed { index, player ->
            items.add(
                PlayerViewModel(
                    order = index,
                    player = player,
                    baselineMinimum = first().points,
                    baselineMaximum = baselineMaximum
                )
            )
            player.counters.forEach { counter ->
                if (counter.points - first().points > 0) {
                    items.add(
                        CounterViewModel(
                            player = player,
                            counter = counter,
                            baselineMinimum = first().points,
                            baselineMaximum = baselineMaximum
                        )
                    )
                }
            }
        }
        return if (currentGame.isFinished) items else items.drop(1)
    }
}