package com.rbtgames.boardgame.data.model

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rbtgames.boardgame.R
import java.util.UUID

@Entity(tableName = Player.TABLE_NAME)
data class Player(
    @PrimaryKey @ColumnInfo(name = ID) val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "color") val color: Color = Color.COLOR_1,
    @ColumnInfo(name = "points") val points: Int = 0
) {

    enum class Color(@ColorRes val colorResourceId: Int) {
        COLOR_1(R.color.player_1),
        COLOR_2(R.color.player_2),
        COLOR_3(R.color.player_3),
        COLOR_4(R.color.player_4),
        COLOR_5(R.color.player_5),
        COLOR_6(R.color.player_6),
        COLOR_7(R.color.player_7),
        COLOR_8(R.color.player_8),
        COLOR_9(R.color.player_9),
        COLOR_10(R.color.player_10),
        COLOR_11(R.color.player_11),
        COLOR_12(R.color.player_12),
        COLOR_13(R.color.player_13),
        COLOR_14(R.color.player_14),
        COLOR_15(R.color.player_15),
        COLOR_16(R.color.player_16)
    }

    companion object {
        const val TABLE_NAME = "playerIds"
        const val ID = "id"
    }
}