package com.rbtgames.boardgame

interface WindowObserver {

    val statusBarHeight: Int
    var keyboardHeight: Int

    fun setTranslucentStatusBar(enable: Boolean)
}