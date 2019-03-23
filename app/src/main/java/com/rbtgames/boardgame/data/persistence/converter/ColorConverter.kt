package com.rbtgames.boardgame.data.persistence.converter

import androidx.room.TypeConverter
import com.rbtgames.boardgame.data.model.Player

class ColorConverter {

    @TypeConverter
    fun loadList(colorName: String): Player.Color = Player.Color.valueOf(colorName)

    @TypeConverter
    fun saveList(color: Player.Color) = color.name
}