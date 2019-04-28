package com.rbtgames.boardgame.feature.shared

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.CallSuper
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.parcel.Parcelize


open class StateSavingAppBarLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppBarLayout(context, attrs) {

    private var isAppBarExpanded = false
    private val onOffsetChangedListener = OnOffsetChangedListener { _, offset -> onOffsetChanged(offset) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnOffsetChangedListener(onOffsetChangedListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnOffsetChangedListener(onOffsetChangedListener)
    }

    @CallSuper
    protected open fun onOffsetChanged(offset: Int) {
        isAppBarExpanded = offset == 0
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? SavedState?)?.isExpanded?.also { isExpanded ->
            super.onRestoreInstanceState(state.state)
            isAppBarExpanded = isExpanded
            setExpanded(isExpanded, false)
        }
    }

    override fun onSaveInstanceState() = SavedState(super.onSaveInstanceState(), isAppBarExpanded)

    @Parcelize
    data class SavedState(val state: Parcelable?, val isExpanded: Boolean) : BaseSavedState(state), Parcelable
}