package com.rbtgames.boardgame.feature.home.games.playerDetail

import androidx.lifecycle.LiveData
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.feature.home.games.playerDetail.list.ColorViewModel

class PlayerDetailViewModel(private val game: Game, private val player: Player) : ScreenViewModel() {

    val shouldNavigateBack: LiveData<Boolean?> get() = _shouldNavigateBack
    private val _shouldNavigateBack = eventLiveData()
    val playerName = mutableLiveDataOf(player.name)
    val isDoneButtonEnabled: LiveData<Boolean> get() = _isDoneButtonEnabled
    private val _isDoneButtonEnabled = mutableLiveDataOf(player.name.isNotEmpty())
    val colors: LiveData<List<ColorViewModel>> get() = _colors
    private val _colors = mutableLiveDataOf(Player.Color.values().map { color ->
        ColorViewModel(
            color = color,
            isSelected = color.colorResourceId == player.color.colorResourceId
        )
    })

    init {
        playerName.observeForever { _isDoneButtonEnabled.value = it.isNotEmpty() }
    }

    fun onCloseButtonPressed() = _shouldNavigateBack.sendEvent()

    fun onDoneButtonPressed() {
        if (_isDoneButtonEnabled.value == true) {
            val playerName = playerName.value ?: ""
            val playerColor = _colors.value?.find { it.isSelected }?.color ?: Player.Color.values().first()
            game.players.apply {
                val currentPlayer = find { it.id == player.id }
                if (currentPlayer == null) {
                    add(Player(player.id, playerName, playerColor))
                } else {
                    currentPlayer.name = playerName
                    currentPlayer.color = playerColor
                }
            }
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