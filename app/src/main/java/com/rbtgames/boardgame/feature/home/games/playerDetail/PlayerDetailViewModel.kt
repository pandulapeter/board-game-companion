package com.rbtgames.boardgame.feature.home.games.playerDetail

import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.feature.ScreenViewModel
import com.rbtgames.boardgame.utils.eventLiveData
import com.rbtgames.boardgame.utils.mutableLiveDataOf
import com.rbtgames.boardgame.utils.sendEvent

class PlayerDetailViewModel(private val game: Game, private val player: Player) : ScreenViewModel() {

    val shouldNavigateBack = eventLiveData()
    val playerName = mutableLiveDataOf(player.name)
    val isDoneButtonEnabled = mutableLiveDataOf(player.name.isNotEmpty())
    val colors = mutableLiveDataOf(Player.Color.values().map { color ->
        ColorViewModel(
            color = color,
            isSelected = color.colorResourceId == player.color.colorResourceId
        )
    })

    init {
        playerName.observeForever { isDoneButtonEnabled.value = it.isNotEmpty() }
    }

    fun onCloseButtonPressed() = shouldNavigateBack.sendEvent()

    fun onDoneButtonPressed() {
        if (isDoneButtonEnabled.value == true) {
            val playerName = playerName.value ?: ""
            val playerColor = colors.value?.find { it.isSelected }?.color ?: Player.Color.values().first()
            game.players.apply {
                val currentPlayer = find { it.id == player.id }
                if (currentPlayer == null) {
                    add(Player(player.id, playerName, playerColor))
                } else {
                    currentPlayer.name = playerName
                    currentPlayer.color = playerColor
                }
            }
            shouldNavigateBack.sendEvent()
        }
    }

    fun onColorSelected(selectedColor: Player.Color) {
        colors.value = Player.Color.values().map { color ->
            ColorViewModel(
                color = color,
                isSelected = color.colorResourceId == selectedColor.colorResourceId
            )
        }
    }
}