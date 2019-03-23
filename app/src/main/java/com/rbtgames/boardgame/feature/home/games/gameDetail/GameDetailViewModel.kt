package com.rbtgames.boardgame.feature.home.games.gameDetail

import androidx.lifecycle.LiveData
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
    val nextPlayerColor: LiveData<Player.Color> get() = _nextPlayerColor
    private val _nextPlayerColor = mutableLiveDataOf(Player.Color.COLOR_1)
    val nextPlayerName: LiveData<String> get() = _nextPlayerName
    private val _nextPlayerName = mutableLiveDataOf("")
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
        points.value = ""
    }

    private fun refreshList() {
        launch(Dispatchers.Default) {
            val players = gameRepository.getGame(gameId)!!.players.sortedByDescending { it.points }.mapIndexed { index, player -> PlayerViewModel(index, player) }
            val nextPlayer = players.first().player
            launch(Dispatchers.Main) {
                _players.value = players
                _nextPlayerColor.value = nextPlayer.color
                _nextPlayerName.value = nextPlayer.name
            }
        }
    }
}