package com.rbtgames.boardgame.feature.shared

import android.view.View
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.utils.color
import com.rbtgames.boardgame.utils.visible

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    visible = isVisible
}

@BindingAdapter("color")
fun CardView.setColor(color: Player.Color) {
    setCardBackgroundColor(context.color(color.colorResourceId))
}
