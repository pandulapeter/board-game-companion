package com.rbtgames.boardgame.feature.home.games.playerDetail

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.repository.GameRepository
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.playerDetail.list.ColorViewModel

class PlayerDetailViewModel(private val gameRepository: GameRepository, private val playerId: String, private val gameId: String) : ScreenViewModel() {

    private val defaultName = ""
    private val defaultColor = Player.Color.values().first()
    private val game = gameRepository.getGameById(gameId)!!
    private val initialPlayer = game.players.find { it.id == playerId }
    val playerName = mutableLiveDataOf(initialPlayer?.name ?: defaultName)
    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val isDoneButtonEnabled: LiveData<Boolean> get() = _isDoneButtonEnabled
    private val _isDoneButtonEnabled = mutableLiveDataOf(!playerName.value.isNullOrBlank())
    val initialSelectedColorIndex: LiveData<Int> get() = _initialSelectedColorIndex
    private val _initialSelectedColorIndex = mutableLiveDataOf(Player.Color.values().indexOfFirst { it == initialPlayer?.color ?: defaultColor })
    val colors: LiveData<List<ColorViewModel>> get() = _colors
    private val _colors = mutableLiveDataOf(Player.Color.values().mapIndexed { index, color ->
        ColorViewModel(color = color, isSelected = index == _initialSelectedColorIndex.value)
    })

    init {
        playerName.observeForever { _isDoneButtonEnabled.value = it.isNotBlank() }
    }

    fun onCloseButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onDoneButtonPressed() {
        if (_isDoneButtonEnabled.value == true) {
            gameRepository.updateNewGame(
                Game(
                    id = gameId,
                    startTime = game.startTime,
                    players = game.players.toMutableList().apply {
                        val playerName = playerName.value ?: defaultName
                        val playerColor = _colors.value?.find { it.isSelected }?.color ?: defaultColor
                        if (initialPlayer != null) {
                            val index = indexOf(initialPlayer)
                            remove(initialPlayer)
                            add(index, Player(id = initialPlayer.id, name = playerName, color = playerColor))
                        } else {
                            add(0, Player(name = playerName, color = playerColor))
                        }
                    }
                )
            )
            _shouldNavigateBack.sendEvent()
        }
    }

    fun onColorSelected(selectedColor: Player.Color) {
        _colors.value = Player.Color.values().map { color ->
            ColorViewModel(
                color = color,
                isSelected = color.colorResourceId == selectedColor.colorResourceId
            )
        }
    }
}