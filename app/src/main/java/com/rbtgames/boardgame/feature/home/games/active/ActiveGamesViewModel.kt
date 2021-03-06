package com.rbtgames.boardgame.feature.home.games.active

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.list.ActiveGameViewModel
import com.rbtgames.boardgame.feature.home.games.list.GameListListItem
import com.rbtgames.boardgame.feature.home.games.list.HintViewModel
import kotlinx.coroutines.launch

class ActiveGamesViewModel(private val gameRepository: GameRepository) : ScreenViewModel() {

    val listItems: LiveData<List<GameListListItem>> get() = _listItems
    private val _listItems = mutableLiveDataOf(emptyList<GameListListItem>())
    val shouldShowLoadingIndicator: LiveData<Boolean> get() = _shouldShowLoadingIndicator
    private val _shouldShowLoadingIndicator = mutableLiveDataOf(true)
    private var gameToDeleteId: String? = null
    private val games get() = _listItems.value?.filterIsInstance<ActiveGameViewModel>() ?: emptyList()

    fun refreshGames() {
        launch {
            _listItems.postValue(
                gameRepository.getActiveGames()
                    .filter { it.id != gameToDeleteId }
                    .sortedByDescending { it.lastActionTime }
                    .map { game -> ActiveGameViewModel(game) }
                    .toMutableList<GameListListItem>()
                    .apply {
                        when (size) {
                            0 -> add(HintViewModel(R.string.games_active_no_games))
                            else -> add(HintViewModel(R.string.games_active_hint))
                        }
                    })
        }
    }

    fun onLoadingDone() = _shouldShowLoadingIndicator.postValue(false)

    fun canSwipeItem(position: Int) = games.size.let { size ->
        when (size) {
            0 -> false
            else -> position != RecyclerView.NO_POSITION && position < size
        }
    }

    fun deleteGameTemporarily(playerId: String) {
        gameToDeleteId = playerId
        refreshGames()
    }

    fun cancelDeleteGame() {
        gameToDeleteId = null
        refreshGames()
    }

    fun hasGameToDelete() = gameToDeleteId != null

    fun deleteGamePermanently() {
        gameToDeleteId?.let {
            launch { gameRepository.deleteGame(it) }
            gameToDeleteId = null
        }
    }
}