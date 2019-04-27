package com.rbtgames.boardgame.feature.shared

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
import com.rbtgames.boardgame.data.model.Player
import com.rbtgames.boardgame.utils.animateCircularReveal
import com.rbtgames.boardgame.utils.color
import com.rbtgames.boardgame.utils.visible

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visible = isVisible
}

@BindingAdapter("color")
fun CardView.setColor(color: Player.Color?) {
    color?.let { setCardBackgroundColor(context.color(color.colorResourceId)) }
}

@BindingAdapter(value = ["color", "revealView"])
fun AppBarLayout.setColor(color: Player.Color?, revealView: View) {
    color?.let {
        val newColor = context.color(color.colorResourceId)
        revealView.setBackgroundColor(newColor)
        revealView.animateCircularReveal {
            setBackgroundColor(newColor)
            revealView.visible = false
        }
    }
}

@BindingAdapter("color")
fun ProgressBar.setColor(color: Player.Color?) {
    color?.let { progressDrawable = progressDrawable?.mutate()?.apply { setTint(context.color(color.colorResourceId)) } }
}

@BindingAdapter("animatedProgress")
fun ProgressBar.setAnimatedProgress(value: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setProgress(value, true)
    } else {
        progress = value
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("time", "prefix")
fun TextView.setTime(timeInMilliseconds: Long, prefix: String) {
    text = prefix + " " + DateUtils.formatDateTime(context, timeInMilliseconds, DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME)
}

@BindingAdapter("android:text")
fun setText(view: EditText, text: String?) {
    if (!TextUtils.equals(view.text, text)) {
        view.setText(text)
        if (text != null) {
            view.setSelection(text.length)
        }
    }
}