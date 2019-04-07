package com.rbtgames.boardgame.feature.shared

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.tabs.TabLayout


open class TouchBlockinTabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : TabLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}