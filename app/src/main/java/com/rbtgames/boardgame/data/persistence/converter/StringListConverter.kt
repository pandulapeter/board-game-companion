package com.rbtgames.boardgame.data.persistence.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()

    @TypeConverter
    fun loadList(listOfString: String): List<String> = gson.fromJson(listOfString, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun saveList(listOfString: List<String>) = gson.toJson(listOfString) ?: ""
}