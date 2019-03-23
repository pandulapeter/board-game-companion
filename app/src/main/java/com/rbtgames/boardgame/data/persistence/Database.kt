package com.rbtgames.boardgame.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rbtgames.boardgame.data.model.Game
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.data.persistence.converter.ColorConverter
import com.rbtgames.boardgame.data.persistence.converter.StringListConverter
import com.rbtgames.boardgame.data.persistence.dao.GameDao
import com.rbtgames.boardgame.data.persistence.dao.PlayerDao

@Database(entities = [Game::class, Player::class], version = 1, exportSchema = false)
@TypeConverters(value = [StringListConverter::class, ColorConverter::class])
abstract class Database : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao
}