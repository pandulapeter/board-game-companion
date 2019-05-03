package com.rbtgames.boardgame.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Counter(
    @Json(name = "id") val id: String = UUID.randomUUID().toString(),
    @Json(name = "name") val name: String = "",
    @Json(name = "points") val points: Int = 0
)