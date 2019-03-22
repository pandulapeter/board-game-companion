package com.rbtgames.boardgame.feature.shared

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.utils.color
import com.rbtgames.boardgame.utils.visible

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visible = isVisible
}

@BindingAdapter("color")
fun CardView.setColor(color: Player.Color) {
    setCardBackgroundColor(context.color(color.colorResourceId))
}

@BindingAdapter("time")
fun TextView.setTime(timeInMilliseconds: Long) {
    text = DateUtils.formatDateTime(context, timeInMilliseconds, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)
}