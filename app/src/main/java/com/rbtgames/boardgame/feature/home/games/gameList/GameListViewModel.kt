package com.rbtgames.boardgame.feature.home.games.gameList

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.R
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameListListItem
import com.rbtgames.boardgame.feature.home.games.gameList.list.GameViewModel
import com.rbtgames.boardgame.feature.home.games.gameList.list.HintViewModel

class GameListViewModel(private val gameRepository: GameRepository) : ScreenViewModel() {

    val shouldOpenNewGameScreen: LiveData<Boolean?> get() = _shouldOpenNewGameScreen
    private val _shouldOpenNewGameScreen = eventLiveData()
    val listItems: LiveData<List<GameListListItem>> get() = _listItems
    private val _listItems = mutableLiveDataOf(emptyList<GameListListItem>())

    fun refreshGames() {
        _listItems.value = gameRepository.getAllGames().sortedByDescending { it.startTime }.let { games ->
            games.map { GameViewModel(it) }.toMutableList<GameListListItem>().apply {
                when (size) {
                    0 -> add(HintViewModel(R.string.game_list_no_games))
                    else -> add(HintViewModel(R.string.game_list_hint))
                }
            }
        }
    }

    fun onNewButtonPressed() = _shouldOpenNewGameScreen.sendEvent()
}