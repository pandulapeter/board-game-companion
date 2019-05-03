package com.rbtgames.boardgame.data.repository

import com.rbtgames.boardgame.data.model.Counter
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.persistence.Database
import com.rbtgames.boardgame.data.persistence.model.FullGame
import com.rbtgames.boardgame.data.persistence.model.GameEntity
import com.rbtgames.boardgame.data.persistence.model.PlayerEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(database: Database) {

    private val gameDao = database.fullGameDao()
    private var newGame: Game? = null
    private val counterAdapter by lazy { Moshi.Builder().build().adapter<List<Counter>>(Types.newParameterizedType(List::class.java, Counter::class.java)) }

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

    suspend fun getActiveGames() = withContext(Dispatchers.Default) {
        gameDao.getActiveGames().map { it.toGame() }
    }

    suspend fun getFinishedGames() = withContext(Dispatchers.Default) {
        gameDao.getFinishedGames().map { it.toGame() }
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
        players = playerEntities.map { it.toPlayer() },
        isFinished = isFinished
    )

    private fun PlayerEntity.toPlayer() = Player(
        id = id,
        name = name,
        color = color,
        points = points,
        counters = counterAdapter.fromJson(counters) ?: listOf()
    )

    private fun Game.toGameEntity() = GameEntity(
        id = id,
        startTime = startTime,
        lastActionTime = lastActionTime,
        isFinished = isFinished
    )

    private fun List<Player>.toPlayerEntity(gameId: String) = map { player ->
        PlayerEntity(
            id = player.id,
            gameId = gameId,
            name = player.name,
            color = player.color,
            points = player.points,
            counters = counterAdapter.toJson(player.counters)
        )
    }
}