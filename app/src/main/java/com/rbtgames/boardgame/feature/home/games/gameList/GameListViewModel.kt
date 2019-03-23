package com.rbtgames.boardgame.feature.home.games.gameList

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.data.repository.PlayerRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameListListItem
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameViewModel
import com.rbtgames.boardgame.feature.home.games.gameList.list.HintViewModel
import kotlinx.coroutines.launch

class GameListViewModel(private val gameRepository: GameRepository, private val playerRepository: PlayerRepository) : ScreenViewModel() {

    val shouldOpenNewGameScreen: LiveData<Boolean?> get() = _shouldOpenNewGameScreen
    private val _shouldOpenNewGameScreen = eventLiveData()
    val listItems: LiveData<List<GameListListItem>> get() = _listItems
    private val _listItems = mutableLiveDataOf(emptyList<GameListListItem>())
    private var gameToDeleteId: String? = null
    private val games get() = _listItems.value?.filterIsInstance<GameViewModel>() ?: emptyList()

    fun refreshGames() {
        launch {
            _listItems.postValue(
                gameRepository.getAllGames()
                    .asSequence()
                    .filter { it.id != gameToDeleteId }
                    .sortedByDescending { it.lastActionTime }
                    .toList()
                    .let { games ->
                        games
                            .map { game ->
                                GameViewModel(
                                    game = game,
                                    nextPlayerName = game.playerIds.mapNotNull { playerId -> playerRepository.getPlayer(playerId) }.minBy { it.points }?.name ?: ""
                                )
                            }
                            .toMutableList<GameListListItem>()
                            .apply {
                                when (size) {
                                    0 -> add(HintViewModel(R.string.game_list_no_games))
                                    else -> add(HintViewModel(R.string.game_list_hint))
                                }
                            }
                    })
        }
    }

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

    fun deleteGamePermanently() {
        gameToDeleteId?.let {
            gameRepository.deleteGame(it)
            gameToDeleteId = null
        }
    }

    fun onNewButtonPressed() = _shouldOpenNewGameScreen.sendEvent()
}