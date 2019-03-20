package com.rbtgames.boardgame.utils

import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.rbtgames.boardgame.data.model.Player

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    visible = isVisible
}

@BindingAdapter("color")
fun CardView.setColor(color: Player.Color) {
    setCardBackgroundColor(context.color(color.colorResourceId))
}
