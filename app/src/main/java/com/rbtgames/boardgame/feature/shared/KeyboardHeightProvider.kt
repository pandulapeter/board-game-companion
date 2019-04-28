package com.rbtgames.boardgame.feature.shared

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import com.rbtgames.boardgame.R

internal class KeyboardHeightProvider(private val activity: Activity) : PopupWindow(activity) {

    private var resizableView: View
    private var parentView: View
    private var lastKeyboardHeight = -1
    private var keyboardListeners = ArrayList<KeyboardListener>()
    private val topCutoutHeight: Int
        get() {
            val decorView = activity.window.decorView ?: return 0
            var cutOffHeight = 0
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val windowInsets = decorView.rootWindowInsets
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    val displayCutout = windowInsets.displayCutout
                    if (displayCutout != null) {
                        val list = displayCutout.boundingRects
                        for (rect in list) {
                            if (rect.top == 0) {
                                cutOffHeight += rect.bottom - rect.top
                            }
                        }
                    }
                }

            }
            return cutOffHeight
        }

    init {
        contentView = View.inflate(activity, R.layout.activity, null)
        resizableView = contentView.findViewById(R.id.fragment_container)
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED
        width = 0
        height = WindowManager.LayoutParams.MATCH_PARENT
        parentView = activity.findViewById(android.R.id.content)
        resizableView.viewTreeObserver.addOnGlobalLayoutListener(getGlobalLayoutListener())
    }

    private fun getGlobalLayoutListener() = ViewTreeObserver.OnGlobalLayoutListener { computeKeyboardState() }

    private fun computeKeyboardState() {
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)
        val rect = Rect()
        resizableView.getWindowVisibleDisplayFrame(rect)
        val keyboardHeight = screenSize.y + topCutoutHeight - rect.bottom
        KeyboardInfo.keyboardState = if (keyboardHeight > 0) KeyboardInfo.STATE_OPENED else KeyboardInfo.STATE_CLOSED
        if (keyboardHeight > 0) {
            KeyboardInfo.keyboardHeight = keyboardHeight
        }
        if (keyboardHeight != lastKeyboardHeight)
            notifyKeyboardHeightChanged(keyboardHeight)
        lastKeyboardHeight = keyboardHeight
    }

    fun addKeyboardListener(listener: KeyboardListener) {
        keyboardListeners.add(listener)
    }

    fun removeKeyboardListener(listener: KeyboardListener) {
        keyboardListeners.remove(listener)
    }

    private fun notifyKeyboardHeightChanged(height: Int) {
        keyboardListeners.forEach {
            it.onHeightChanged(Math.max(0, height))
        }
    }

    fun start() {
        if (!isShowing && parentView.windowToken != null) {
            showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    fun close() {
        this.keyboardListeners.clear()
        dismiss()
    }

    interface KeyboardListener {
        fun onHeightChanged(height: Int)
    }

    private object KeyboardInfo {
        const val HEIGHT_NOT_COMPUTED = -1
        const val STATE_UNKNOWN = -1
        const val STATE_CLOSED = 0
        const val STATE_OPENED = 1

        var keyboardHeight = HEIGHT_NOT_COMPUTED
        var keyboardState = STATE_UNKNOWN
    }
}