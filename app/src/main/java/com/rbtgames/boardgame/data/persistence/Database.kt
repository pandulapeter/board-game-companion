package com.rbtgames.boardgame.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rbtgames.boardgame.data.persistence.converter.ColorConverter
import com.rbtgames.boardgame.data.persistence.dao.FullGameDao
import com.rbtgames.boardgame.data.persistence.model.GameEntity
import com.rbtgames.boardgame.data.persistence.model.PlayerEntity

@Database(entities = [GameEntity::class, PlayerEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [ColorConverter::class])
abstract class Database : RoomDatabase() {

    abstract fun fullGameDao(): FullGameDao
}