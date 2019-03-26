package com.rbtgames.boardgame.feature.shared

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.android.parcel.Parcelize

class InsetAwareCoordinatorLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    CoordinatorLayout(context, attrs, defStyleAttr) {

    var statusBarHeight = 0
        private set(value) {
            if (field != value && value != 0) {
                field = value
                windowInsetChangeListener(value)
            }
        }
    var windowInsetChangeListener: ((statusBarHeight: Int) -> Unit) = {}

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        insets?.systemWindowInsetTop?.let {
            statusBarHeight = it
        }
        return super.dispatchApplyWindowInsets(insets)
    }

    override fun onSaveInstanceState() = SavedState(super.onSaveInstanceState(), statusBarHeight)

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SavedState?)?.statusBarHeight?.also { height ->
            super.onRestoreInstanceState(state.state)
            statusBarHeight = height
        }
    }

    @Parcelize
    data class SavedState(val state: Parcelable?, val statusBarHeight: Int) : BaseSavedState(state), Parcelable
}