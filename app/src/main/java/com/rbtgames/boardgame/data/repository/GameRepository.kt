package com.rbtgames.boardgame.data.repository

import com.rbtgames.boardgame.data.model.Game

class GameRepository {

    private var newGame: Game? = null
    private val games = mutableListOf<Game>()

    fun getNewGame() = (newGame ?: Game()).also {
        newGame = it
    }

    fun updateNewGame(game: Game) {
        newGame = game
    }

    fun confirmNewGame(game: Game) {
        games.add(game)
        newGame = null
    }

    fun cancelNewGame() {
        newGame = null
    }

    fun getGameById(id: String) = games.find { it.id == id } ?: newGame

    fun getAllGames(): List<Game> = games

    fun deleteGame(gameId: String) {
        games.removeAll { it.id == gameId }
    }
}