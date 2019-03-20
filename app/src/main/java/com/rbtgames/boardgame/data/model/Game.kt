package com.rbtgames.boardgame.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
    val startTime: Long = System.currentTimeMillis(),
    val players: MutableList<Player> = mutableListOf()
) : Parcelable