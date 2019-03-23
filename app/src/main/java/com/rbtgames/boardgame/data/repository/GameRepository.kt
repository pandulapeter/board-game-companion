package com.rbtgames.boardgame.data.repository

import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.persistence.Database
import com.rbtgames.boardgame.data.persistence.model.FullGame
import com.rbtgames.boardgame.data.persistence.model.GameEntity
import com.rbtgames.boardgame.data.persistence.model.PlayerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(database: Database) {

    private val gameDao = database.fullGameDao()
    private var newGame: Game? = null

    fun getNewGame() = (newGame ?: Game()).also {
        newGame = it
    }

    fun updateNewGame(game: Game) {
        newGame = game
    }

    suspend fun confirmNewGame(game: Game) = withContext(Dispatchers.Default) {
        gameDao.insertGame(game.toGameEntity())
        gameDao.insertPlayers(game.players.toPlayerEntity(game.id))
        newGame = null
    }

    fun cancelNewGame() {
        newGame = null
    }

    suspend fun getGame(gameId: String) = withContext(Dispatchers.Default) {
        gameDao.getGame(gameId)?.toGame() ?: newGame
    }

    suspend fun getAllGames() = withContext(Dispatchers.Default) {
        gameDao.getAllGames().map { it.toGame() }
    }

    suspend fun updateGame(game: Game) = withContext(Dispatchers.Default) {
        gameDao.insertGame(game.toGameEntity())
        gameDao.insertPlayers(game.players.toPlayerEntity(game.id))
    }

    suspend fun deleteGame(gameId: String) = withContext(Dispatchers.Default) {
        gameDao.deleteGame(gameId)
    }

    private fun FullGame.toGame() = Game(
        id = id,
        startTime = startTime,
        lastActionTime = lastActionTime,
        players = playerEntities.map { it.toPlayer() }
    )

    private fun PlayerEntity.toPlayer() = Player(
        id = id,
        name = name,
        color = color,
        points = points
    )

    private fun Game.toGameEntity() = GameEntity(
        id = id,
        startTime = startTime,
        lastActionTime = lastActionTime
    )

    private fun List<Player>.toPlayerEntity(gameId: String) = map { player ->
        PlayerEntity(
            id = player.id,
            gameId = gameId,
            name = player.name,
            color = player.color,
            points = player.points
        )
    }
}